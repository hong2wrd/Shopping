package com.shopping.item.mapper;

import com.shopping.item.dto.response.ItemRespDto;
import com.shopping.item.entity.Book;
import com.shopping.item.entity.Item;
import com.shopping.item.entity.ItemType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static ItemRespDto toDto(Book book) {
        ItemRespDto response = ItemRespDto.builder()
                .itemId(book.getId())
                .itemType(ItemType.BOOK)
                .author(book.getAuthor())
                .build();

        response.setName(book.getName());
        response.setPrice(book.getPrice());
        response.setQuantity(book.getStockQuantity());

        return response;
    }

    public static ItemRespDto toDto(Item item) {
        ItemRespDto response = ItemRespDto.builder()
                .itemId(item.getId())
                .build();

        response.setName(item.getName());
        response.setPrice(item.getPrice());
        response.setQuantity(item.getStockQuantity());

        return response;
    }

}