package com.shopping.common.outbox.dto;

public record OutboxMessage(
        String topic,
        String key,
        String payload
) {
}
