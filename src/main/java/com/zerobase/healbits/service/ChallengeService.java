package com.zerobase.healbits.service;

import com.zerobase.healbits.domain.Challenge;
import com.zerobase.healbits.domain.Member;
import com.zerobase.healbits.dto.ChallengeDto;
import com.zerobase.healbits.dto.ChallengeSummaryInfo;
import com.zerobase.healbits.dto.RegisterChallenge;
import com.zerobase.healbits.exception.HealBitsException;
import com.zerobase.healbits.repository.ChallengeRepository;
import com.zerobase.healbits.repository.MemberRepository;
import com.zerobase.healbits.type.ChallengeCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.zerobase.healbits.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public ChallengeDto registerChallenge(RegisterChallenge.Request request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        return ChallengeDto.fromEntity(
                saveAndGetChallenge(request, member)
        );
    }

    public Slice<ChallengeSummaryInfo> getChallengeListByCategory(
            String challengeCategory
            , Pageable pageable
    ) {
        Slice<Challenge> challengeList = challengeRepository.findAllByChallengeCategoryAndEndDateGreaterThanEqual(
                ChallengeCategory.convertStringToChallengeCategory(challengeCategory), LocalDate.now(), pageable
        );
        return challengeList.map(ChallengeSummaryInfo::fromEntity);
    }

    public ChallengeDto getChallengeDetail(long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new HealBitsException(CHALLENGE_NOT_FOUND));
        return ChallengeDto.fromEntity(challenge);
    }

    private void validateStartAndEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            throw new HealBitsException(END_DATE_INVALID);
        }

        if (LocalDate.now().isAfter(startDate)) {
            throw new HealBitsException(START_DATE_INVALID);
        }
    }

    private Challenge saveAndGetChallenge(RegisterChallenge.Request request, Member member) {
        return challengeRepository.save(Challenge.builder()
                .registeredMember(member)
                .challengeName(request.getChallengeName())
                .challengeCategory(ChallengeCategory
                        .convertStringToChallengeCategory(
                                request.getChallengeCategory())
                )
                .summary(request.getSummary())
                .contents(request.getContents())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build());
    }


}
