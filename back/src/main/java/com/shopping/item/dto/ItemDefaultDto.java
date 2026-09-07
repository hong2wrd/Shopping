package com.shopping.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
public class ItemDefaultDto {

    @NotBlank(message = "상품명은 필수 입력 항목입니다.")
    private String name;

    @Positive(message = "가격은 0원보다 커야 합니다.")
    private int price;

    @Min(value = 0, message = "재고가 0보다 작을 수 없습니다.")
    private int quantity;

}
