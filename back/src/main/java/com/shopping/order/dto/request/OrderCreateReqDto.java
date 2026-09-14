package com.shopping.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderCreateReqDto {
    @Min(value = 0, message = "총 금액은 0 이상이어야 합니다.")
    private int totalPrice;
    @Min(value = 0, message = "총 할인 금액은 0 이상이어야 합니다.")
    private int totalDiscountPrice;
    @Min(value = 0, message = "총 할인 비율은 0 이상이어야 합니다.")
    private double totalDiscountRate;
    private List<OrderItemCreateReqDto> orderItems;

    @Setter
    @Getter
    public static class OrderItemCreateReqDto {
        @NotNull(message = "상품 정보(ID)는 필수 사항입니다.")
        private Long itemId;

        @Min(value = 1, message = "상품 수량은 1 이상이어야 합니다.")
        private int quantity;
    }
}


