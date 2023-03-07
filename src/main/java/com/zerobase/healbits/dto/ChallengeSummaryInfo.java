package com.zerobase.healbits.dto;

import com.zerobase.healbits.domain.Challenge;
import com.zerobase.healbits.type.ChallengeCategory;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeSummaryInfo {

    private String challengeName;
    private ChallengeCategory challengeCategory;
    private String summary;
    private long participantsNum;
    private long duration;
    private LocalDate startDate;
    private LocalDate endDate;

    public static ChallengeSummaryInfo fromEntity(Challenge challenge) {
        return ChallengeSummaryInfo.builder()
                .challengeName(challenge.getChallengeName())
                .challengeCategory(challenge.getChallengeCategory())
                .summary(challenge.getSummary())
                .participantsNum(challenge.getParticipantsNum())
                .duration(Period.between(challenge.getStartDate(), challenge.getEndDate()).getDays())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .build();
    }
}
