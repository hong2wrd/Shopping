package com.shopping.member.dto.response;

public record MemberRespDto(
        String memId,
        String name,
        String memberShip,
        String role,
        boolean retired,
        String leaveReason) {

}
