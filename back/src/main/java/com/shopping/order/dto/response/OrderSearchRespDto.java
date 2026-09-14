package com.shopping.order.dto.response;

public record OrderSearchRespDto(
        long itemId,
        String itemName,
        int amount,
        int quantity
) {
}
