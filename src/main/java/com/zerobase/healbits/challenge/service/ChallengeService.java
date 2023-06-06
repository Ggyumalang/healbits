package com.zerobase.healbits.challenge.service;

import com.zerobase.healbits.challenge.domain.Challenge;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.challenge.dto.ChallengeDto;
import com.zerobase.healbits.challenge.dto.ChallengeSummaryInfo;
import com.zerobase.healbits.challenge.dto.RegisterChallenge;
import com.zerobase.healbits.common.exception.HealBitsException;
import com.zerobase.healbits.challenge.repository.ChallengeRepository;
import com.zerobase.healbits.member.repository.MemberRepository;
import com.zerobase.healbits.common.type.ChallengeCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.zerobase.healbits.common.type.ErrorCode.*;

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
            ChallengeCategory challengeCategory
            , Pageable pageable
    ) {
        Slice<Challenge> challengeList = challengeRepository.findAllByChallengeCategoryAndEndDateGreaterThanEqual(
                challengeCategory, LocalDate.now(), pageable
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
            throw new HealBitsException(INVALID_END_DATE);
        }

        if (LocalDate.now().isAfter(startDate)) {
            throw new HealBitsException(INVALID_START_DATE);
        }
    }

    private Challenge saveAndGetChallenge(RegisterChallenge.Request request, Member member) {
        return challengeRepository.save(Challenge.builder()
                .registeredMember(member)
                .challengeName(request.getChallengeName())
                .challengeCategory(request.getChallengeCategory())
                .summary(request.getSummary())
                .contents(request.getContents())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build());
    }


}
