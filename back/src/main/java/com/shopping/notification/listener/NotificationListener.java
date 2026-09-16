package com.shopping.notification.listener;

import com.shopping.notification.entity.Message;
import com.shopping.notification.service.NotificationService;
import com.shopping.payment.event.PaymentApprovedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;
    private final ObjectMapper om;

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

}
