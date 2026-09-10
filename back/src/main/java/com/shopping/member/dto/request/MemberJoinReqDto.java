package com.shopping.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberJoinReqDto {

    @NotBlank(message = "사용자 ID는 필수 사항입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "영문/숫자 5~20자 이내로 작성해주세요.")
    private String memId;
    @NotBlank(message = "사용자 이름은 필수 사항입니다.")
    private String name;
    @NotBlank(message = "비밀번호는 필수 사항입니다.")
    private String password;
}
