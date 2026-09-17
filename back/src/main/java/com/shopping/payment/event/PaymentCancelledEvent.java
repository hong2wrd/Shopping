package com.shopping.payment.event;

import java.time.LocalDate;

public record PaymentCancelledEvent(
        String eventId,
        String orderNumber,
        Long paymentId,
        long amount,
        LocalDate occurredAt,
        String memId
) {
}
