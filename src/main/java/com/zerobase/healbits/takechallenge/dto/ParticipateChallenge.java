package com.zerobase.healbits.takechallenge.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ParticipateChallenge {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private long challengeId;

        @NotNull
        @Min(1)
        private long participationFee;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private String email;

        private String challengeName;

        private long participationFee;

        public static ParticipateChallenge.Response from(TakeChallengeDto dto) {
            return Response.builder()
                    .email(dto.getEmail())
                    .challengeName(dto.getChallengeName())
                    .participationFee(dto.getParticipationFee())
                    .build();
        }
    }
}
