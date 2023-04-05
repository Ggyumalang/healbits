package com.zerobase.healbits.common.exception;

import com.zerobase.healbits.common.type.ErrorCode;
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
