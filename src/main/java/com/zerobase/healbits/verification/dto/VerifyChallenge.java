package com.zerobase.healbits.verification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class VerifyChallenge {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String email;
        private String challengeName;
        private LocalDate verifiedDate;
        private String imagePath;

        public static Response from(VerificationDto dto) {
            return Response.builder()
                    .email(dto.getEmail())
                    .challengeName(dto.getChallengeName())
                    .verifiedDate(dto.getVerifiedDate())
                    .imagePath(dto.getImagePath())
                    .build();
        }
    }
}
