package com.zerobase.healbits.verification.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate verifiedDate;

    private String imageUrl;

    public static VerificationDateAndImages fromEntity(Verification verification, AwsS3Api awsS3Api) {
        return VerificationDateAndImages.builder()
                .verifiedDate(verification.getVerifiedDate())
                .imageUrl(awsS3Api.getImageUrl(verification.getImagePath()))
                .build();
    }
}
