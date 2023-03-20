package com.zerobase.healbits.takechallenge.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TakeChallengeInfo {
    String challengeName;
    String summary;
    LocalDate startDate;
    LocalDate endDate;
}
