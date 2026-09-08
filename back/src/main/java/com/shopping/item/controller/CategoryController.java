package com.shopping.item.controller;

import com.shopping.common.dto.ResponseDto;
import com.shopping.item.dto.request.CategorySaveReqDto;
import com.shopping.item.dto.request.CategoryUpdateReqDto;
import com.shopping.item.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(
                new ResponseDto<>(1, "모든 카테고리를 조회하였습니다.", categoryService.getCategory())
        );
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getAllCategories(@PathVariable Long categoryId) {
        return ResponseEntity.ok(
                new ResponseDto<>(1, "요청한 카테고리를 조회하였습니다.",categoryService.getCategory(categoryId))
        );
    }

    @PostMapping("/{categoryName}")
    public ResponseEntity<?> saveCategory(@PathVariable String categoryName) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDto<>(1, "카테고리를 생성하였습니다.", categoryService.saveCategory(categoryName))
        );
    }

    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody CategorySaveReqDto categorySaveReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDto<>(1, "카테고리를 생성하였습니다.", categoryService.saveCategory(categorySaveReqDto))
        );
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody CategoryUpdateReqDto categoryUpdateReqDto) {
        return ResponseEntity.ok(
                new ResponseDto<>(1, "카테고리를 수정하였습니다.", categoryService.updateCategory(categoryUpdateReqDto))
        );
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(new ResponseDto<>(1, "카테고리를 삭제하였습니다."));
    }

}
