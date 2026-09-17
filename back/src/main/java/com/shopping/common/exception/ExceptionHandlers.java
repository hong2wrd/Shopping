package com.shopping.common.exception;

import com.shopping.common.dto.ResponseDto;
import com.shopping.item.exception.InsufficientStockException;
import com.shopping.item.exception.InvalidItemValueException;
import com.shopping.member.exception.MemberValidationException;
import com.shopping.notification.exception.NotificationValidException;
import com.shopping.order.exception.OrderValidationException;
import com.shopping.payment.exception.PaymentValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> bindExceptionHandler(BindException e) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : e.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        ResponseDto<?> responseDto = new ResponseDto<>(-1, errorMap.isEmpty() ? "알 수 없는 에러" : "유효성검사 실패", errorMap);

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> businessExceptionHandler(BusinessException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), e.getHttpStatus());
    }

    @ExceptionHandler(MemberValidationException.class)
    public ResponseEntity<?> memberValidationExceptionHandler(MemberValidationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidItemValueException.class)
    public ResponseEntity<?> invalidItemValueExceptionHandler(InvalidItemValueException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> insufficientStockExceptionHandler(InsufficientStockException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OrderValidationException.class)
    public ResponseEntity<?> orderValidationExceptionHandler(OrderValidationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentValidationException.class)
    public ResponseEntity<?> paymentValidationExceptionHandler(PaymentValidationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotificationValidException.class)
    public ResponseEntity<?> notificationValidExceptionHandler(NotificationValidException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("Unhandled exception", e);
        return new ResponseEntity<>(new ResponseDto<>(-1, "서버 내부 오류가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
