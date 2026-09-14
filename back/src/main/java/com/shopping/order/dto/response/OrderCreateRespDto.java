package com.shopping.order.dto.response;

public record OrderCreateRespDto (
        String orderNumber, String paymentAttemptId) {
}
