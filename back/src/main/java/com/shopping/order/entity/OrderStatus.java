package com.shopping.order.entity;

public enum OrderStatus {
    RECEIVED("주문 접수"),
    PAYMENT_PENDING("결제 대기"),
    PAYMENT_APPROVED("결제 완료"),
    PREPARING_PRODUCT("상품 준비 중"),
    READY_FOR_SHIPPING("배송 중"),
    SHIPPED("배송 완료"),
    ORDER_COMPLETED("구매 확정"),
    CANCELED("주문 취소"),
    RETURN_REQUESTED("반품 신청"),
    RETURN_RECEIVED("반품 입고"),
    REFUNDED("환불 완료"),
    EXCHANGE_REQUESTED("교환 신청")
    ;

    private String msg;

    OrderStatus(String msg) {
        this.msg = msg;
    }
}
