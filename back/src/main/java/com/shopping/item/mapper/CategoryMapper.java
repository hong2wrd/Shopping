package com.shopping.item.mapper;

import com.shopping.item.dto.response.CategoryRespDto;
import com.shopping.item.entity.Category;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {

    public static CategoryRespDto toCategoryRespDto(Category category) {
        return CategoryRespDto.builder()
                .id(category.getId())
                .name(category.getName())
                .children(category.getChildren()
                        .stream()
                        .map(CategoryMapper::toCategoryRespDto)
                        .toList())
                .items(category.getItems()
                        .stream()
                        .map(ItemMapper::toDto)
                        .toList())
                .build();
    }

}
