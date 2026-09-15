package com.shopping.order.entity;

import com.shopping.common.entity.BaseEntity;
import com.shopping.member.entity.Member;
import com.shopping.order.exception.OrderValidationException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String orderNumber;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.RECEIVED;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int totalDiscountPrice;

    @Column(nullable = false)
    private double totalDiscountRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private static final String ORD_PREFIX = "ORD-";

    /**
     * 주문번호 생성 / ORD-YYYYMMDD-UUID(8)
     */
    @PrePersist
    private void createOrderNumber() {
        this.orderNumber = ORD_PREFIX
                + LocalDate
                    .now(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.BASIC_ISO_DATE)
                + "-"
                + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 주문 생성
     * @param totalPrice 총 금액
     * @param totalDiscountPrice 총 할인 금액
     * @param totalDiscountRate 총 할인 비율
     * @return Order
     */
    public static Order create(int totalPrice, int totalDiscountPrice, double totalDiscountRate, Member member) {
        if(totalPrice < 0) {
            throw new OrderValidationException("주문 금액은 0원 이상이어야 합니다.");
        }

        if(totalDiscountPrice < 0) {
            throw new OrderValidationException("주문 할인 금액은 0원 이상이어야 합니다.");
        }

        if(totalDiscountRate < 0) {
            throw new OrderValidationException("주문 할인 비율은 0 이상이어야 합니다.");
        }

        if(member == null) {
            throw new OrderValidationException("회원 정보가 존재하지 않습니다.");
        }

        return Order.builder()
                .totalPrice(totalPrice)
                .totalDiscountPrice(totalDiscountPrice)
                .totalDiscountRate(totalDiscountRate)
                .member(member)
                .build();
    }

    /**
     * 결제 완료
     */
    public void paymentComplete() {
        if(this.status != OrderStatus.RECEIVED) {
            throw new OrderValidationException("주문 접수 상태에서만 결제 완료가 가능합니다.");
        }

        this.status = OrderStatus.PAYMENT_APPROVED;
    }

    /**
     * 주문 취소
     * <br>
     * - 주문 접수 상태에서만 주문 취소가 가능합니다.
     */
    public void orderCancel() {
        if(this.status != OrderStatus.RECEIVED) {
            throw new OrderValidationException("주문 접수에만 주문 취소가 가능합니다.");
        }

        this.status = OrderStatus.CANCELED;
    }

    public void orderPaymentApproved() {
        if(this.status != OrderStatus.RECEIVED) {
            throw new OrderValidationException("주문 접수 상태에서만 결재완료가 가능합니다.");
        }

        this.status = OrderStatus.PAYMENT_APPROVED;
    }
}