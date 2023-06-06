package com.zerobase.healbits.challenge.dto;

import com.zerobase.healbits.challenge.type.ChallengeCategory;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class RegisterChallenge {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {

        @NotBlank
        private String challengeName;

        private ChallengeCategory challengeCategory;

        private String summary;

        @Lob
        private String contents;

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate startDate;

        @NotNull
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate endDate;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private String email;

        private String challengeName;

        public static Response from(ChallengeDto challengeDto) {
            return Response.builder()
                    .email(challengeDto.getEmail())
                    .challengeName(challengeDto.getChallengeName())
                    .build();
        }
    }
}
