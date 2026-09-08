package com.shopping.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CategorySaveReqDto {
    @NotBlank(message = "카테고리 이름은 필수 입력값입니다.")
    private String name;

    @NotNull(message = "상위 카테고리는 필수 입력값입니다.")
    private Long parentId;

    private List<Long> itemIds;
}
