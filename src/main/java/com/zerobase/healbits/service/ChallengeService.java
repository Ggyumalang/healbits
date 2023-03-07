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

import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.healbits.type.ErrorCode.CHALLENGE_CATEGORY_NOT_FOUND;
import static com.zerobase.healbits.type.ErrorCode.EMAIL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    public ChallengeDto registerChallenge(RegisterChallenge.Request request) {

        return ChallengeDto.fromEntity(
                saveAndGetChallenge(request, getMemberByEmail(request.getEmail()))
        );
    }

    public List<ChallengeSummaryInfo> getChallengeListByCategory(String challengeCategory) {
        List<Challenge> challengeList = challengeRepository.findAllByChallengeCategory(
                convertStringToChallengeCategory(challengeCategory)
        );
        return challengeList.stream()
                .map(ChallengeSummaryInfo::fromEntity)
                .collect(Collectors.toList());
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));
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
