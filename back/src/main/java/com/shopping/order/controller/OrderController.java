package com.shopping.order.controller;

import com.shopping.order.dto.request.OrderCreateReqDto;
import com.shopping.order.dto.request.OrderCancelReqDto;
import com.shopping.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> searchByOrderNumber(@RequestParam("orderNumber") String orderNumber,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.searchByOrderNumber(orderNumber, userDetails.getUsername()));
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchByMemberId(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.searchByMemberId(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody @Valid OrderCreateReqDto orderCreateReqDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.saveOrder(orderCreateReqDto, userDetails.getUsername()));
    }

    @PatchMapping
    public ResponseEntity<Void> cancelOrder(@RequestBody @Valid OrderCancelReqDto reqDto,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        orderService.cancelOrder(reqDto.getOrderId(), reqDto.getAttemptId(), userDetails.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
