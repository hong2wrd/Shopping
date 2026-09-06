package com.shopping.common.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginReqDto {
    private String memId;
    private String password;
}
