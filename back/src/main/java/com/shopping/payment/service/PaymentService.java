package com.shopping.payment.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.order.entity.Order;
import com.shopping.order.repository.OrderRepository;
import com.shopping.payment.dto.request.PaymentApprovedReqDto;
import com.shopping.payment.entity.Payment;
import com.shopping.payment.entity.PaymentMethod;
import com.shopping.payment.entity.PaymentStatus;
import com.shopping.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    /**
     * 결제 생성
     *
     * @param orderId 주문 ID
     * @param memId   회원 ID
     *
     * @return 결제 시도 ID
     */
    public String createPayment(long orderId, String memId) {
        Order findOrder = orderRepository.findByIdAndMember_MemId(orderId, memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        long amount = findOrder.getTotalPrice() - findOrder.getTotalDiscountPrice();

        Payment payment = paymentRepository.save(Payment.create(findOrder, amount));

        return payment.getAttemptId();
    }

    public void processPayment(String attemptId) {
        List<PaymentStatus> status = List.of(PaymentStatus.READY, PaymentStatus.IN_PROGRESS, PaymentStatus.FAILED);
        Payment payment = paymentRepository.findByAttemptIdAndStatusIn(attemptId, status)
                .orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));

        payment.process();
    }

    public void approvePayment(PaymentApprovedReqDto approvedReqDto, String memId) {
        orderRepository.findByOrderNumberAndMember_MemId(approvedReqDto.getOrderNumber(), memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        Payment payment = paymentRepository
                .findByAttemptIdAndStatusIn(approvedReqDto.getAttemptId(),List.of(PaymentStatus.IN_PROGRESS))
                .orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));

        payment.approve(approvedReqDto.getPaymentKey(), PaymentMethod.valueOf(approvedReqDto.getPaymentMethod()));
    }

    /**
     * 결제 취소
     * @param attemptId 결제 시도 ID
     */
    public void cancelPayment(String attemptId, long orderId, String memId) {
        orderRepository.findByIdAndMember_MemId(orderId, memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        List<PaymentStatus> status = List.of(PaymentStatus.IN_PROGRESS, PaymentStatus.APPROVED, PaymentStatus.PARTIAL_CANCELLED);

        Payment payment = paymentRepository.findByAttemptIdAndStatusIn(attemptId, status)
                .orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));

        payment.cancel();
    }
}
