package com.zerobase.healbits.exception;

import com.zerobase.healbits.dto.ErrorResponse;
import com.zerobase.healbits.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.zerobase.healbits.type.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HealBitsException.class)
    public ErrorResponse handleHealBitsException(HealBitsException he){
        log.error("{} is occured." , he.getErrorCode());
        return ErrorResponse.builder()
                .errorCode(he.getErrorCode())
                .errorMessage(he.getErrorMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ErrorResponse handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ie){
        log.error("{} is occured", ie.getMessage());
        return ErrorResponse.builder()
                .errorCode(ErrorCode.EMAIL_NOT_FOUND)
                .errorMessage(ErrorCode.EMAIL_NOT_FOUND.getErrorMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException be){
        log.error("{} is occured", be.getMessage());
        return ErrorResponse.builder()
                .errorCode(ErrorCode.WRONG_PASSWORD_ERROR)
                .errorMessage(ErrorCode.WRONG_PASSWORD_ERROR.getErrorMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidExcpetion(MethodArgumentNotValidException me){
        log.error("{} is occured", me.getMessage());
        return ErrorResponse.builder()
                .errorCode(ErrorCode.WRONG_PHONE_FORMAT)
                .errorMessage(me.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e){
        log.error("Exception is occured." , e);
        return ErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getErrorMessage())
                .build();
    }
}
