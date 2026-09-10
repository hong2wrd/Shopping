package com.shopping.member.entity;

import lombok.Getter;

@Getter
public enum MemberShip {
    BRONZE(0), SILVER(2.5), GOLD(5), PLATINUM(7.5), VIP(10);

    private double discount;

    MemberShip(double discount) {
        this.discount = discount;
    }
}
