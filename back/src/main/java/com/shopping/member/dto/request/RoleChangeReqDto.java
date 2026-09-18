package com.shopping.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleChangeReqDto {
    @NotBlank(message = "회원 아이디는 필수사항입니다.")
    private String memId;
    @NotBlank(message = "변경할 권한은 필수사항입니다.")
    private String changeRole;
}
