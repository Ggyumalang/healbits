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
public class ChallengeInfo {

    private String challengeName;
    private ChallengeCategory challengeCategory;
    private String summary;
    private String contents;
    private long participantsNum;
    private LocalDate startDate;
    private LocalDate endDate;

    public static ChallengeInfo fromEntity(Challenge challenge) {
        return ChallengeInfo.builder()
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
