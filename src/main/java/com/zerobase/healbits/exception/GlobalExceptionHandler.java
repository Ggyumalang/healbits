package com.zerobase.healbits.exception;

import com.zerobase.healbits.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ErrorResponse handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ie) {
        log.error("EMAIL NOT FOUND", ie);
        return ErrorResponse.builder()
                .errorCode(EMAIL_NOT_FOUND)
                .errorMessage(EMAIL_NOT_FOUND.getErrorMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException be) {
        log.error("{} is occurred", be.getMessage());
        return ErrorResponse.builder()
                .errorCode(WRONG_PASSWORD_ERROR)
                .errorMessage(WRONG_PASSWORD_ERROR.getErrorMessage())
                .build();
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
