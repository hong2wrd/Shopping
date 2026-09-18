package com.shopping.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberUpdateReqDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;
}
