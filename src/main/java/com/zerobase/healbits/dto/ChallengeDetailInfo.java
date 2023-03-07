package com.zerobase.healbits.dto;

import com.zerobase.healbits.type.ChallengeCategory;
import com.zerobase.healbits.type.ChallengeStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeDetailInfo {

    private String challengeName;
    private ChallengeCategory challengeCategory;
    private String contents;
    private long participantsNum;
    private ChallengeStatus challengeStatus;
    private LocalDate startDate;
    private LocalDate endDate;

    public static ChallengeDetailInfo from(ChallengeDto dto) {
        return ChallengeDetailInfo.builder()
                .challengeName(dto.getChallengeName())
                .challengeCategory(dto.getChallengeCategory())
                .contents(dto.getContents())
                .participantsNum(dto.getParticipantsNum())
                .challengeStatus(getChallengeStatus(dto.getStartDate(), dto.getEndDate()))
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    private static ChallengeStatus getChallengeStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();
        if(now.isBefore(startDate) || now.isEqual(startDate)){
            return ChallengeStatus.READY;
        }else if(now.isBefore(endDate) || now.isEqual(endDate)){
            return ChallengeStatus.IN_PROGRESS;
        }else {
            return ChallengeStatus.CLOSED;
        }
    }
}
