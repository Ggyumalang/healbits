package com.zerobase.healbits.exception;

import com.zerobase.healbits.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealBitsException extends RuntimeException{

    private ErrorCode errorCode;
    private String errorMessage;

    public HealBitsException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getErrorMessage();
    }
}
