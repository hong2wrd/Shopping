package com.shopping.item.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class CategoryRespDto {
    private Long id;
    private String name;
    private List<CategoryRespDto> children;
    private List<ItemRespDto> items;
}
