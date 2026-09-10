package com.shopping.item.controller;

import com.shopping.common.dto.ResponseDto;
import com.shopping.common.security.MemberDetails;
import com.shopping.item.dto.request.ItemSaveReqDto;
import com.shopping.item.dto.request.ItemUpdateReqDto;
import com.shopping.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ResponseEntity<?> searchItem(@PathVariable("itemId") Long itemId) {
        return ResponseEntity
                .ok(new ResponseDto<>(1, "상품을 조회하였습니다.", itemService.searchItem(itemId)));
    }

    @PostMapping
    public ResponseEntity<?> saveItem(@RequestBody @Valid ItemSaveReqDto itemSaveReqDto,
                                      @AuthenticationPrincipal MemberDetails memberDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto<>(1, "상품을 등록하였습니다.", itemService.saveItem(memberDetails.getUsername(), itemSaveReqDto)));
    }

    @PutMapping
    public ResponseEntity<?> updateItem(@RequestBody @Valid ItemUpdateReqDto totalItemSaveReqDto,
                                        @AuthenticationPrincipal MemberDetails memberDetails) {
        return ResponseEntity
                .ok(new ResponseDto<>(1, "상품을 수정하였습니다.", itemService.updateItem(memberDetails.getUsername(), totalItemSaveReqDto)));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId,
                                        @AuthenticationPrincipal MemberDetails memberDetails) {

        itemService.deleteItem(memberDetails.getUsername(), itemId);

        return ResponseEntity
                .ok(new ResponseDto<>(1, "상품을 삭제하였습니다."));
    }
}
