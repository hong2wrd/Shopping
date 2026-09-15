package com.shopping.common.exception;

public class OutboxValidationException extends RuntimeException {
    public OutboxValidationException(String message) {
        super(message);
    }
}
