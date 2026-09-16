package com.shopping.payment.entity;

import com.shopping.common.entity.BaseEntity;
import com.shopping.order.entity.Order;
import com.shopping.payment.exception.PaymentValidationException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_payment_order")
    )
    private Order order;

    @Column(nullable = false)
    private String attemptId;

    @Column
    private String paymentKey;

    @Column(nullable = false, updatable = false)
    private long amount;

    private long cancelAmount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method = PaymentMethod.PENDING;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.READY;

    private LocalDateTime approvedAt;
    private LocalDateTime cancelledAt;

    private String failureCode;

    private String failureMessage;

    private static final String PAY_PREFIX = "PAY-";

    @PrePersist
    private void createAttemptId() {
        this.attemptId = PAY_PREFIX
                + LocalDate
                    .now(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.BASIC_ISO_DATE)
                + "-"
                + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 결제 생성
     * @param order         주문 정보
     * @param amount        결제 금액
     * @return Payment
     */
    public static Payment create(Order order, long amount) {
        if(order == null) {
            throw new PaymentValidationException("주문 정보는 필수 사항입니다.");
        }

        if(amount <= 0) {
            throw new PaymentValidationException("결제 금액은 0보다 커야 합니다.");
        }

        return Payment.builder()
                .order(order)
                .amount(amount)
                .build();
    }

    /**
     * 결제 진행
     */
    public void process() {
        List<PaymentStatus> status = List.of(PaymentStatus.READY, PaymentStatus.IN_PROGRESS, PaymentStatus.FAILED);
        if(!status.contains(this.status)) {
            throw new PaymentValidationException("이미 결제 처리가 완료된 상태입니다.");
        }

        this.status = PaymentStatus.IN_PROGRESS;
    }

    /**
     * 결제 승인
     */
    public void approve(String paymentKey, PaymentMethod paymentMethod) {
        if(paymentKey == null || paymentKey.isBlank()) {
            throw new PaymentValidationException("결제 키가 유효하지 않습니다.");
        }

        if(paymentMethod == null) {
            throw new PaymentValidationException("결제 수단이 유효하지 않습니다.");
        }

        List<PaymentStatus> failed = List.of(
                PaymentStatus.APPROVED,
                PaymentStatus.FAILED,
                PaymentStatus.CANCELLED
                );

        if(failed.contains(this.status)) {
            throw new PaymentValidationException("이미 결제 처리가 완료된 상태입니다.");
        }

        this.status = PaymentStatus.APPROVED;
        this.paymentKey = paymentKey;
        this.method = paymentMethod;
        this.approvedAt = LocalDateTime.now();
    }

    /**
     * 결제 취소
     */
    public void cancel() {
        List<PaymentStatus> approved = List.of(PaymentStatus.APPROVED);
        if(!approved.contains(this.status)) {
            throw new PaymentValidationException("결제 처리가 완료되지 않은 상태입니다.");
        }

        this.status = PaymentStatus.CANCELLED;
        this.cancelAmount = this.amount;
        this.cancelledAt = LocalDateTime.now();
    }

    /**
     * 결제 실패 정보 설정
     * @param failureCode     결제 실패 코드
     * @param failureMessage  결제 실패 메시지s
     */
    public void failure(String failureCode, String failureMessage) {
        if(failureCode == null || failureCode.isBlank()) {
            throw new PaymentValidationException("결제 실패 코드는 필수 사항입니다.");
        }

        if(failureMessage == null || failureMessage.isBlank()) {
            throw new PaymentValidationException("결제 실패 메시지는 필수 사항입니다.");
        }

        this.status = PaymentStatus.FAILED;
        this.failureCode = failureCode;
        this.failureMessage = failureMessage;
    }

}
