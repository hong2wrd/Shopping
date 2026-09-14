package com.shopping.order.controller;

import com.shopping.common.security.MemberDetails;
import com.shopping.order.dto.request.OrderCreateReqDto;
import com.shopping.order.dto.request.OrderCancelReqDto;
import com.shopping.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> searchByOrderNumber(@RequestParam("orderNumber") String orderNumber,
                                                 @AuthenticationPrincipal MemberDetails memberDetails) {
        return ResponseEntity.ok(orderService.searchByOrderNumber(orderNumber, memberDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody @Valid OrderCreateReqDto orderCreateReqDto,
                                       @AuthenticationPrincipal MemberDetails memberDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.saveOrder(orderCreateReqDto, memberDetails.getUsername()));
    }

    @PatchMapping
    public ResponseEntity<Void> cancelOrder(@RequestBody @Valid OrderCancelReqDto reqDto,
                                            @AuthenticationPrincipal MemberDetails memberDetails) {
        orderService.cancelOrder(reqDto.getOrderId(), reqDto.getAttemptId(), memberDetails.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
