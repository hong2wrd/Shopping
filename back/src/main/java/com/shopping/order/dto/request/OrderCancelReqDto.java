package com.shopping.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderCancelReqDto {
    @NotNull(message = "상품ID가 누락되었습니다.")
    private Long orderId;

    @NotNull(message = "결제 시도 ID가 누락되었습니다.")
    private String attemptId;

}
