package com.zerobase.healbits.verification.dto;

import com.zerobase.healbits.takechallenge.domain.TakeChallenge;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationInfo {

    private List<VerificationDateAndImages> dateAndImagesList;

    private long verifiedCnt;

    private long remainVerificationCnt;

    private String verificationRate;

    public void setVerificationStatistics(TakeChallenge takeChallenge) {

        LocalDate startDate = takeChallenge.getParticipatedChallenge().getStartDate();
        LocalDate endDate = takeChallenge.getParticipatedChallenge().getEndDate();
        int totalCnt = Period.between(startDate, endDate).getDays() + 1;

        verifiedCnt = dateAndImagesList.size();
        remainVerificationCnt = totalCnt - verifiedCnt;
        verificationRate = String.format("%.2f%%", (double) verifiedCnt / (double) totalCnt * 100);

    }
}
