package com.shopping.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberPasswordUpdateReqDto {
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
