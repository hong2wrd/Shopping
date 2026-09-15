package com.shopping.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentApprovedReqDto {
    @NotBlank(message = "결제 시도 ID는 필수입니다.")
    private String attemptId;

    @NotBlank(message = "결제 키는 필수입니다.")
    private String paymentKey;

    @NotBlank(message = "결제 방법은 필수입니다.")
    private String paymentMethod;

    @NotBlank(message = "주문 번호는 필수입니다.")
    private String orderNumber;
}
