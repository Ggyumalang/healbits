package com.zerobase.healbits.takechallenge.dto;


import com.zerobase.healbits.takechallenge.domain.TakeChallenge;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TakeChallengeDto {

    private String email;

    private String challengeName;

    private long participationFee;

    public static TakeChallengeDto fromEntity(TakeChallenge takeChallenge) {
        return TakeChallengeDto.builder()
                .email(takeChallenge.getParticipatedMember().getEmail())
                .challengeName(takeChallenge.getParticipatedChallenge().getChallengeName())
                .participationFee(takeChallenge.getParticipationFee())
                .build();
    }
}
