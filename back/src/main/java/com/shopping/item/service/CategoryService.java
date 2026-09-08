package com.shopping.item.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.item.dto.request.CategorySaveReqDto;
import com.shopping.item.dto.request.CategoryUpdateReqDto;
import com.shopping.item.dto.response.CategoryRespDto;
import com.shopping.item.entity.Category;
import com.shopping.item.entity.Item;
import com.shopping.item.mapper.CategoryMapper;
import com.shopping.item.repository.CategoryRepository;
import com.shopping.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    /**
     * 모든 카테고리 조회
     * @return 카테고리 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<CategoryRespDto> getCategory() {
        return categoryRepository.findAllByParentIsNull().stream()
                .map(CategoryMapper::toCategoryRespDto)
                .toList();
    }

    /**
     * 카테고리 조회
     * @param id 카테고리 ID
     * @return 카테고리 DTO
     */
    @Transactional(readOnly = true)
    public CategoryRespDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        return CategoryMapper.toCategoryRespDto(category);
    }

    /**
     * 카테고리 저장
     * @param name 카테고리 이름
     * @return 카테고리 DTO
     */
    public CategoryRespDto saveCategory(String name) {
        Category category = categoryRepository.save(Category.create(name));
        return CategoryMapper.toCategoryRespDto(category);
    }

    /**
     * 카테고리 저장
     * @param reqDto 카테고리 요청 DTO
     * @return 카테고리 DTO
     */
    public CategoryRespDto saveCategory(CategorySaveReqDto reqDto) {
        Long parentId = Optional.ofNullable(reqDto.getParentId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_PARENT_NOT_FOUND));

        Category findParent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        List<Item> findItems = getFindItems(reqDto.getItemIds());

        Category category = Category.create(reqDto.getName());

        findItems.forEach(category::addItem);
        findParent.addChildCategory(category);

        categoryRepository.save(category);

        return CategoryMapper.toCategoryRespDto(findParent);
    }

    /**
     * 카테고리 변경
     * @param reqDto 카테고리 변경 DTO
     * @return 카테고리 DTO
     */
    public CategoryRespDto updateCategory(CategoryUpdateReqDto reqDto) {

        Category findCategory = categoryRepository.findById(reqDto.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Category parentCategory = reqDto.getParentId() == null
                ? null
                : categoryRepository.findById(reqDto.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        findCategory.changeParent(parentCategory);

        findCategory.updateName(reqDto.getName());

        List<Item> findItems = getFindItems(reqDto.getItemIds());

        List.copyOf(findCategory.getItems()).stream()
                .filter(item -> !findItems.contains(item))
                .forEach(findCategory::removeItem);

        findItems.forEach(findCategory::changeItem);

        return CategoryMapper.toCategoryRespDto(findCategory);
    }

    /**
     * 카테고리 삭제
     * @param id 카테고리 id
     */
    public void deleteCategory(Long id) {
        Category findCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        List.copyOf(findCategory.getItems()).forEach(findCategory::removeItem);

        findCategory.removeCategory();

        categoryRepository.delete(findCategory);
    }

    /**
     * 상품 엔티티 리스트 찾기
     * @param dtoItemIds 카테고리에 저장된 상품 아이디 리스트
     * @return 카테고리에 저장된 상품 엔티티
     */
    private List<Item> getFindItems(List<Long> dtoItemIds) {
        List<Long> itemIds = Optional.ofNullable(dtoItemIds).orElse(List.of());

        List<Item> findItems = itemRepository.findAllById(itemIds);

        if (findItems.size() != itemIds.stream().distinct().count()) {
            throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
        }
        return findItems;
    }
}
