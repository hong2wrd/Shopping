package com.shopping.item.mapper;

import com.shopping.item.dto.response.ItemRespDto;
import com.shopping.item.entity.culture.Book;
import com.shopping.item.entity.Item;
import com.shopping.item.entity.ItemType;
import com.shopping.item.entity.clothes.Bottom;
import com.shopping.item.entity.clothes.Outer;
import com.shopping.item.entity.clothes.Shoes;
import com.shopping.item.entity.clothes.Top;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static ItemRespDto toDto(Item item) {

        ItemRespDto itemRespDto = ItemRespDto.builder()
                .itemId(item.getId())
                .build();

        if(item instanceof Book book) {
            itemRespDto.setItemType(ItemType.BOOK);
            itemRespDto.setAuthor(book.getAuthor());
        } else if(item instanceof Top top) {
            itemRespDto.setItemType(ItemType.TOP);
            itemRespDto.setSize(top.getSize().toString());
        } else if(item instanceof Bottom bottom) {
            itemRespDto.setItemType(ItemType.BOTTOM);
            itemRespDto.setSize(bottom.getSize().toString());
        } else if(item instanceof Outer outer) {
            itemRespDto.setItemType(ItemType.OUTER);
            itemRespDto.setSize(outer.getSize().toString());
        } else if(item instanceof Shoes shoes) {
            itemRespDto.setItemType(ItemType.SHOES);
            itemRespDto.setSize(shoes.getSize().toString());
        }

        return itemRespDto;
    }

}