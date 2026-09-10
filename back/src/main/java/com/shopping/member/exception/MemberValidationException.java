package com.shopping.member.exception;

public class MemberValidationException extends RuntimeException{
    public MemberValidationException(String message) {
        super(message);
    }
}
