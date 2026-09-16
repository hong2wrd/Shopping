package com.shopping.common.outbox.exception;

public class OutboxValidationException extends RuntimeException {
    public OutboxValidationException(String message) {
        super(message);
    }
}
