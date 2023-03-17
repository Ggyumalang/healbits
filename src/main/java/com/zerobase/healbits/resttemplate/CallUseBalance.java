package com.zerobase.healbits.resttemplate;

import lombok.*;

public class CallUseBalance {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Request{
        private long participationFee;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Response{
        private String path;
        private String error;
        private String message;
        private int status;
    }

}
