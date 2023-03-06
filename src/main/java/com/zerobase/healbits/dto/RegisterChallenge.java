package com.zerobase.healbits.dto;

import com.zerobase.healbits.type.ChallengeCategory;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class RegisterChallenge {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {

        @Email
        private String email;

        @NotBlank
        private String challengeName;

        @NotBlank
        private String challengeCategory;

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

        private ChallengeCategory challengeCategory;

        private String summary;

        private LocalDate startDate;

        private LocalDate endDate;

        public static Response from(ChallengeDto challengeDto) {
            return Response.builder()
                    .email(challengeDto.getEmail())
                    .challengeName(challengeDto.getChallengeName())
                    .challengeCategory(challengeDto.getChallengeCategory())
                    .summary(challengeDto.getSummary())
                    .startDate(challengeDto.getStartDate())
                    .endDate(challengeDto.getEndDate())
                    .build();
        }
    }
}
