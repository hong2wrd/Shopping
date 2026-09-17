package com.shopping.notification.listener;

import com.shopping.notification.entity.Message;
import com.shopping.notification.service.NotificationService;
import com.shopping.payment.event.PaymentApprovedEvent;
import com.shopping.payment.event.PaymentCancelledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;
    private final ObjectMapper om;

    /**
     * 결제 승인 이벤트 수신
     * @param payload 결제 승인 이벤트
     */
    @KafkaListener(
            topics = "#{T(com.shopping.common.kafka.Topic).PAYMENT_APPROVED.getName()}",
            groupId = "payment-notification"
    )
    public void listenPaymentApproved(String payload) {
        PaymentApprovedEvent paymentApprovedEvent = om.readValue(payload, PaymentApprovedEvent.class);

        notificationService.sendNotification(
                paymentApprovedEvent.orderNumber(),
                Message.ORDER_COMPLETED,
                paymentApprovedEvent.memId()
        );
    }

    /**
     * 결제 취소 이벤트 수신
     * @param payload 결제 취소 이벤트
     */
    @KafkaListener(
            topics = "#{T(com.shopping.common.kafka.Topic).PAYMENT_CANCELLED.getName()}",
            groupId = "payment-notification"
    )
    public void listenPaymentCancelled(String payload) {
        PaymentCancelledEvent paymentCancelledEvent = om.readValue(payload, PaymentCancelledEvent.class);

        notificationService.sendNotification(
                paymentCancelledEvent.orderNumber(),
                Message.ORDER_CANCELLED,
                paymentCancelledEvent.memId()
        );
    }

}
