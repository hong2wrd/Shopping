package com.shopping.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

    private Map<String, String> errorMap;
    private HttpStatus httpStatus;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();
    }

    public BusinessException(ErrorCode errorCode, Map<String, String> errorMap) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();
        this.errorMap = errorMap;
    }
}
