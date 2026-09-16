package com.shopping.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderSearchListRespDto(
        String orderNumber,
        String orderStatus,
        LocalDateTime orderDate,
        List<OrderSearchRespDto> items
) {
}
