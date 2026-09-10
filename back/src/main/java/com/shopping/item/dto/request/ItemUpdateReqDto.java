package com.shopping.item.dto.request;

import com.shopping.item.entity.ItemType;
import com.shopping.item.entity.clothes.Size;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemUpdateReqDto {

    @NotNull(message = "상품 ID는 필수 입력값입니다.")
    private Long itemId;

    @NotNull(message = "상품 타입은 필수입니다.")
    private ItemType itemtype;

    @NotBlank(message = "상품명은 필수 입력 항목입니다.")
    private String name;

    @Positive(message = "가격은 0원보다 커야 합니다.")
    private int price;

    private String author;

    private Size size;

}
