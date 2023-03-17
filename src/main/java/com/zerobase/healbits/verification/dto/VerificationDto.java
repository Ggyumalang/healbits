package com.zerobase.healbits.verification.dto;

import com.zerobase.healbits.verification.domain.Verification;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationDto {

    private String email;

    private String challengeName;

    private LocalDate verifiedDate;

    private String imagePath;

    public static VerificationDto fromEntity(Verification verification) {
        return VerificationDto.builder()
                .email(verification.getVerifiedTakeChallenge().getParticipatedMember().getEmail())
                .challengeName(verification.getVerifiedTakeChallenge().getParticipatedChallenge().getChallengeName())
                .verifiedDate(verification.getVerifiedDate())
                .imagePath(verification.getImagePath())
                .build();
    }
}
