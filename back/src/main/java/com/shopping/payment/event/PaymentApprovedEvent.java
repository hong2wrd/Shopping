package com.shopping.payment.event;

import java.time.LocalDate;

public record PaymentApprovedEvent(
        String eventId,
        Long orderId,
        Long paymentId,
        long amount,
        LocalDate occurredAt
) {
}
