package com.zerobase.healbits.verification.dto;

import com.zerobase.healbits.awss3.AwsS3Api;
import com.zerobase.healbits.verification.domain.Verification;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationDateAndImages {

    private LocalDate verifiedDate;

    private String imageUrl;

    public static VerificationDateAndImages fromEntity(Verification verification, AwsS3Api awsS3Api) {
        return VerificationDateAndImages.builder()
                .verifiedDate(verification.getVerifiedDate())
                .imageUrl(awsS3Api.getImageUrl(verification.getImagePath()))
                .build();
    }
}
