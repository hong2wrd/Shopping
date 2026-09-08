package com.shopping.item.dto.response;

import com.shopping.item.dto.ItemDefaultDto;
import com.shopping.item.entity.ItemType;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRespDto extends ItemDefaultDto {
    private Long itemId;
    private ItemType itemType;

    private String author;

    private String size;
}
