package com.zerobase.healbits.takechallenge.service;

import com.zerobase.healbits.challenge.domain.Challenge;
import com.zerobase.healbits.challenge.repository.ChallengeRepository;
import com.zerobase.healbits.exception.HealBitsException;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.member.repository.MemberRepository;
import com.zerobase.healbits.resttemplate.RestTemplateApi;
import com.zerobase.healbits.takechallenge.domain.TakeChallenge;
import com.zerobase.healbits.takechallenge.dto.ParticipateChallenge;
import com.zerobase.healbits.takechallenge.dto.TakeChallengeDto;
import com.zerobase.healbits.takechallenge.repository.TakeChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.zerobase.healbits.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TakeChallengeService {

    private final TakeChallengeRepository takeChallengeRepository;

    private final ChallengeRepository challengeRepository;

    private final MemberRepository memberRepository;

    private final RestTemplateApi restTemplateApi;

    @Transactional
    public TakeChallengeDto participateChallenge(ParticipateChallenge.Request request, String email, String token) {
        Challenge challenge = challengeRepository.findById(request.getChallengeId())
                .orElseThrow(() -> new HealBitsException(CHALLENGE_NOT_FOUND));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        validateParticipateChallenge(challenge, member, request.getParticipationFee());

        challenge.increaseParticipantsNum();

        restTemplateApi.callUseBalance(request.getParticipationFee(), token);

        return TakeChallengeDto.fromEntity(
                saveAndGetTakeChallenge(challenge, member, request.getParticipationFee())
        );

    }

    private void validateParticipateChallenge(Challenge challenge, Member member, long participationFee) {

        if (takeChallengeRepository.existsByParticipatedChallengeIdAndParticipatedMemberId(
                challenge.getId(), member.getId()
        )) {
            throw new HealBitsException(ALREADY_PARTICIPATED_CHALLENGE);
        }

        if (LocalDate.now().isAfter(challenge.getStartDate())) {
            throw new HealBitsException(INVALID_START_DATE);
        }

        if (participationFee <= 0) {
            throw new HealBitsException(INVALID_AMOUNT);
        }

        if (member.getBalance() < participationFee) {
            throw new HealBitsException(AMOUNT_EXCEED_BALANCE);
        }

    }

    private TakeChallenge saveAndGetTakeChallenge(Challenge challenge, Member member, long participationFee) {
        return takeChallengeRepository.save(TakeChallenge.builder()
                .participatedChallenge(challenge)
                .participatedMember(member)
                .participationFee(participationFee)
                .build());
    }
}
