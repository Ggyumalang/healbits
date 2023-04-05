package com.zerobase.healbits.verification.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.zerobase.healbits.verification.domain.Verification;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public static VerificationDateAndImages fromEntity(
        Verification verification, String imageUrl) {
        return VerificationDateAndImages.builder()
            .verifiedDate(verification.getVerifiedDate())
            .imageUrl(imageUrl)
            .build();
    }
}
