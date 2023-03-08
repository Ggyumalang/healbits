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
@ToString
@Builder
public class ChallengeSummaryInfo {

    private String challengeName;
    private ChallengeCategory challengeCategory;
    private String summary;
    private long participantsNum;
    private long challengeDuration;
    private long remainingDaysToStart;

    public static ChallengeSummaryInfo fromEntity(Challenge challenge) {
        return ChallengeSummaryInfo.builder()
                .challengeName(challenge.getChallengeName())
                .challengeCategory(challenge.getChallengeCategory())
                .summary(challenge.getSummary())
                .participantsNum(challenge.getParticipantsNum())
                .challengeDuration(getDuration(challenge.getStartDate(), challenge.getEndDate())+1)
                .remainingDaysToStart(getDuration(LocalDate.now(), challenge.getStartDate()))
                .build();
    }

    private static long getDuration(LocalDate startDate, LocalDate targetDate) {
        return Period.between(startDate, targetDate).getDays();
    }
}
