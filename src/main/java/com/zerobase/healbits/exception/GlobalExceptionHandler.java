package com.zerobase.healbits.exception;

import com.zerobase.healbits.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e){
        log.error("Exception is occured." , e);
        return ErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getErrorMessage())
                .build();
    }
}
