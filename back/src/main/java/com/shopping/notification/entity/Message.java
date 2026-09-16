package com.shopping.notification.entity;

import lombok.Getter;

@Getter
public enum Message {
    ORDER_COMPLETED("상품 구매가 완료되었습니다.")
    ;

    private String message;

    Message(String message) {
        this.message = message;
    }
}
