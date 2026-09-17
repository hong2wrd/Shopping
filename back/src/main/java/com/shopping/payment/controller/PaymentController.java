package com.shopping.payment.controller;

import com.shopping.common.security.MemberDetails;
import com.shopping.payment.dto.request.PaymentApprovedReqDto;
import com.shopping.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PatchMapping("/approved")
    public ResponseEntity<Void> approvePayment(@RequestBody @Valid PaymentApprovedReqDto paymentApprovedReqDto,
                                               @AuthenticationPrincipal MemberDetails userDetails) {
        paymentService.approvePayment(paymentApprovedReqDto, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

}
