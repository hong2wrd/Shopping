package com.shopping.payment.event;

import java.time.LocalDate;

public record PaymentApprovedEvent(
        String eventId,
        String orderNumber,
        Long paymentId,
        long amount,
        LocalDate occurredAt,
        String memId
) {
}
