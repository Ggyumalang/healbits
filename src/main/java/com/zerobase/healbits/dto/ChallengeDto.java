package com.zerobase.healbits.dto;

import com.zerobase.healbits.domain.Challenge;
import com.zerobase.healbits.type.ChallengeCategory;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeDto {

    private String email;
    private String challengeName;
    private ChallengeCategory challengeCategory;
    private String summary;
    private String contents;
    private long participantsNum;
    private LocalDate startDate;
    private LocalDate endDate;

    public static ChallengeDto fromEntity(Challenge challenge) {
        return ChallengeDto.builder()
                .email(challenge.getMember().getEmail())
                .challengeName(challenge.getChallengeName())
                .challengeCategory(challenge.getChallengeCategory())
                .summary(challenge.getSummary())
                .contents(challenge.getContents())
                .participantsNum(challenge.getParticipantsNum())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .build();
    }
}
