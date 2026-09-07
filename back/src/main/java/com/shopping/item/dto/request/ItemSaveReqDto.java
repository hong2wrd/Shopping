package com.shopping.item.dto.request;

import com.shopping.item.dto.ItemDefaultDto;
import com.shopping.item.entity.ItemType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
public class ItemSaveReqDto extends ItemDefaultDto {

    @NotNull(message = "상품 타입은 필수입니다.")
    private ItemType itemtype;
    private String author;
}
