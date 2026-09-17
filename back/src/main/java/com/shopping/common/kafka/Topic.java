package com.shopping.common.kafka;

import lombok.Getter;

@Getter
public enum Topic {
    PAYMENT_APPROVED("shopping.payment.approved"),
    PAYMENT_CANCELLED("shopping.payment.cancelled"),
    ;

    private String name;

    Topic(String name) {
        this.name = name;
    }
}
