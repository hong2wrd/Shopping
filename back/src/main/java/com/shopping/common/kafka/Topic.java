package com.shopping.common.kafka;

import lombok.Getter;

@Getter
public enum Topic {
    PAYMENT_APPROVED("shopping.payment.approved")
    ;

    private String name;

    Topic(String name) {
        this.name = name;
    }
}
