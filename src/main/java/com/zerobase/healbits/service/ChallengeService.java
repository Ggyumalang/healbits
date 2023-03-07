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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.healbits.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    public ChallengeDto registerChallenge(RegisterChallenge.Request request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        return ChallengeDto.fromEntity(
                saveAndGetChallenge(request, member)
        );
    }

    public List<ChallengeSummaryInfo> getChallengeListByCategory(String challengeCategory) {
        List<Challenge> challengeList = challengeRepository.findAllByChallengeCategoryAndEndDateGreaterThanEqual(
                convertStringToChallengeCategory(challengeCategory), LocalDate.now()
        );
        return challengeList.stream()
                .map(ChallengeSummaryInfo::fromEntity)
                .collect(Collectors.toList());
    }

    public ChallengeDto getChallengeDetail(long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new HealBitsException(CHALLENGE_NOT_FOUND));
        return ChallengeDto.fromEntity(challenge);
    }

    private void validateStartAndEndDate(LocalDate startDate, LocalDate endDate) {
        if(endDate.isBefore(startDate)){
            throw new HealBitsException(END_DATE_INVALID);
        }

        if(LocalDate.now().isAfter(startDate)){
            throw new HealBitsException(START_DATE_INVALID);
        }
    }

    private Challenge saveAndGetChallenge(RegisterChallenge.Request request, Member member) {
        return challengeRepository.save(Challenge.builder()
                .member(member)
                .challengeName(request.getChallengeName())
                .challengeCategory(convertStringToChallengeCategory(request.getChallengeCategory()))
                .summary(request.getSummary())
                .contents(request.getContents())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build());
    }

    private static ChallengeCategory convertStringToChallengeCategory(String challengeCategory) {
        try {
            return ChallengeCategory.valueOf(challengeCategory);
        } catch (IllegalArgumentException e) {
            throw new HealBitsException(CHALLENGE_CATEGORY_NOT_FOUND);
        }
    }
}
