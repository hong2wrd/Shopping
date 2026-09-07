package com.shopping.item.dto.request;

import com.shopping.item.dto.ItemDefaultDto;
import com.shopping.item.entity.ItemType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemUpdateReqDto extends ItemDefaultDto {

    @NotNull
    private Long itemId;

    @NotNull(message = "상품 타입은 필수입니다.")
    private ItemType itemtype;
    private String author;

}
