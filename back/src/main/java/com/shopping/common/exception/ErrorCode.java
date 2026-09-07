package com.shopping.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    MEM_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    MEM_CONFLICT(HttpStatus.CONFLICT, "중복된 회원입니다."),
    MEM_CONFIRM(HttpStatus.BAD_REQUEST, "사용자 정보 확인이 필요합니다."),

    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품이 없습니다."),
    ITEM_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 상품입니다."),

    ;

    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
