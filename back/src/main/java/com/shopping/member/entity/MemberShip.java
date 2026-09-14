package com.shopping.member.entity;

import lombok.Getter;

@Getter
public enum MemberShip {
    BRONZE(0), SILVER(2.5), GOLD(5), PLATINUM(7.5), VIP(10);

    private double discount;

    MemberShip(double discount) {
        this.discount = discount;
    }

    /**
     * 회원 맴버쉽 등급에 따른 할인 금액 계산
     * @param amount 금액
     * @return 할인된 금액
     */
    public int getDiscountAmount(int amount) {
        return (int) (amount * (100 - discount / 100));

    }
}
