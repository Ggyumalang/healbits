package com.zerobase.healbits.verification.service;

import com.zerobase.healbits.awss3.AwsS3Api;
import com.zerobase.healbits.exception.HealBitsException;
import com.zerobase.healbits.takechallenge.domain.TakeChallenge;
import com.zerobase.healbits.takechallenge.repository.TakeChallengeRepository;
import com.zerobase.healbits.verification.domain.Verification;
import com.zerobase.healbits.verification.dto.VerificationDateAndImages;
import com.zerobase.healbits.verification.dto.VerificationDto;
import com.zerobase.healbits.verification.dto.VerificationInfo;
import com.zerobase.healbits.verification.respository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zerobase.healbits.type.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final TakeChallengeRepository takeChallengeRepository;

    private final VerificationRepository verificationRepository;

    private final AwsS3Api awsS3Api;

    @Transactional
    public VerificationDto verifyChallenge(long verifiedTakeChallengeId, MultipartFile multipartFile, String email) {
        TakeChallenge takeChallenge = takeChallengeRepository.findById(verifiedTakeChallengeId)
                .orElseThrow(() -> new HealBitsException(TAKE_CHALLENGE_NOT_FOUND));

        validateVerifyChallenge(takeChallenge, multipartFile, email);

        String imagePath = awsS3Api.uploadImage(
                multipartFile
                , takeChallenge.getParticipatedChallenge().getChallengeCategory()
                , takeChallenge.getParticipatedChallenge().getChallengeName()
                , email
        );

        return VerificationDto.fromEntity(
                saveAndGetVerification(takeChallenge, imagePath)
        );
    }

    public VerificationInfo getVerificationInfo(long takeChallengeId) {
        TakeChallenge takeChallenge = takeChallengeRepository.findById(takeChallengeId)
                .orElseThrow(() -> new HealBitsException(TAKE_CHALLENGE_NOT_FOUND));

        List<Verification> verificationList = verificationRepository.findAllByVerifiedTakeChallengeId(takeChallengeId);
        List<VerificationDateAndImages> dateAndImagesList = verificationList.stream()
                .map(x -> VerificationDateAndImages.fromEntity(x, awsS3Api))
                .collect(Collectors.toList());

        VerificationInfo verificationInfo = new VerificationInfo();
        verificationInfo.setDateAndImagesList(dateAndImagesList);
        verificationInfo.setVerificationStatistics(takeChallenge);

        return verificationInfo;
    }

    private Verification saveAndGetVerification(TakeChallenge takeChallenge, String imagePath) {
        return verificationRepository.save(Verification.builder()
                .verifiedTakeChallenge(takeChallenge)
                .verifiedDate(LocalDate.now())
                .imagePath(imagePath)
                .build());
    }

    private void validateVerifyChallenge(TakeChallenge takeChallenge, MultipartFile multipartFile, String email) {
        if (!Objects.equals(takeChallenge.getParticipatedMember().getEmail(), email)) {
            throw new HealBitsException(VERIFICATION_USER_NOT_MATCHED);
        }

        if (multipartFile.isEmpty()) {
            throw new HealBitsException(FILE_NOT_EXIST);
        }

        if (verificationRepository.existsByVerifiedTakeChallengeIdAndVerifiedDate(
                takeChallenge.getId(), LocalDate.now())
        ) {
            throw new HealBitsException(VERIFICATION_ALREADY_EXIST);
        }

        if (LocalDate.now().isBefore(takeChallenge.getParticipatedChallenge().getStartDate())) {
            throw new HealBitsException(CHALLENGE_NOT_STARTED);
        }

        if (LocalDate.now().isAfter(takeChallenge.getParticipatedChallenge().getEndDate())) {
            throw new HealBitsException(CHALLENGE_FINISHED);
        }
    }
}
