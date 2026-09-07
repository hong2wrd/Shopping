package com.shopping.item.controller;

import com.shopping.common.dto.ResponseDto;
import com.shopping.item.dto.request.TotalItemSaveReqDto;
import com.shopping.item.dto.request.TotalItemUpdateReqDto;
import com.shopping.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ResponseEntity<?> searchItem(@PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok(new ResponseDto<>(1, "상품을 조회하였습니다.", itemService.searchItem(itemId)));
    }

    @PostMapping
    public ResponseEntity<?> saveItem(@RequestBody @Valid TotalItemSaveReqDto totalItemSaveReqDto) {

        itemService.saveItem(totalItemSaveReqDto);

        return ResponseEntity.ok(new ResponseDto<>(1, "상품을 등록하였습니다."));
    }

    @PutMapping
    public ResponseEntity<?> updateItem(@RequestBody @Valid TotalItemUpdateReqDto totalItemSaveReqDto) {

        itemService.updateItem(totalItemSaveReqDto);

        return ResponseEntity.ok(new ResponseDto<>(1, "상품을 수정하였습니다."));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId) {

        itemService.deleteItem(itemId);

        return ResponseEntity.ok(new ResponseDto<>(1, "상품을 삭제하였습니다."));
    }
}
