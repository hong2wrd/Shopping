package com.shopping.order.controller;

import com.shopping.common.security.MemberDetails;
import com.shopping.order.dto.request.OrderCreateReqDto;
import com.shopping.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody @Valid OrderCreateReqDto orderCreateReqDto,
                                       @AuthenticationPrincipal MemberDetails memberDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.saveOrder(orderCreateReqDto, memberDetails.getUsername()));
    }
}
