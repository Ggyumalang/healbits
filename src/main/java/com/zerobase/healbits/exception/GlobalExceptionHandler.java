package com.zerobase.healbits.exception;

import com.zerobase.healbits.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.zerobase.healbits.type.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HealBitsException.class)
    public ErrorResponse handleHealBitsException(HealBitsException he) {
        log.error("{} is occurred", he.getErrorCode());
        return ErrorResponse.builder()
                .errorCode(he.getErrorCode())
                .errorMessage(he.getErrorMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException re) {
        log.error("HttpMessageNotReadableException is occurred", re);
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .errorCode(HTTP_MESSAGE_NOT_READABLE)
                .errorMessage(re.getMessage())
                .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException be) {
        log.error("{} is occurred", be.getMessage(), be);
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .errorCode(BAD_CREDENTIALS)
                .errorMessage(be.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        log.error("Exception is occurred", e);
        return ErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getErrorMessage())
                .build();
    }
}
