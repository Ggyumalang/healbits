package com.zerobase.healbits.takechallenge.dto;

import lombok.*;

public class CompleteTookChallenge {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private String challengeName;

        private String verificationRate;

        private long paybackFee;
    }
}
