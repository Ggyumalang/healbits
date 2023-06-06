package com.zerobase.healbits.takechallenge.service;

import com.zerobase.healbits.challenge.domain.Challenge;
import com.zerobase.healbits.challenge.repository.ChallengeRepository;
import com.zerobase.healbits.common.exception.HealBitsException;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.member.repository.MemberRepository;
import com.zerobase.healbits.takechallenge.domain.TakeChallenge;
import com.zerobase.healbits.takechallenge.dto.CompleteTookChallenge;
import com.zerobase.healbits.takechallenge.dto.ParticipateChallenge;
import com.zerobase.healbits.takechallenge.dto.TakeChallengeDto;
import com.zerobase.healbits.takechallenge.dto.TakeChallengeInfo;
import com.zerobase.healbits.takechallenge.repository.TakeChallengeRepository;
import com.zerobase.healbits.transaction.dto.TransactionDto;
import com.zerobase.healbits.transaction.dto.UseBalance;
import com.zerobase.healbits.transaction.service.TransactionService;
import com.zerobase.healbits.challenge.type.ChallengeStatus;
import com.zerobase.healbits.takechallenge.type.PaybackPercentagePolicy;
import com.zerobase.healbits.verification.domain.Verification;
import com.zerobase.healbits.verification.respository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

import static com.zerobase.healbits.common.type.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TakeChallengeService {

    private final TakeChallengeRepository takeChallengeRepository;

    private final ChallengeRepository challengeRepository;

    private final MemberRepository memberRepository;

    private final VerificationRepository verificationRepository;

    private final TransactionService transactionService;

    @Transactional
    public TakeChallengeDto participateChallenge(ParticipateChallenge.Request request, String email) {
        Challenge challenge = challengeRepository.findById(request.getChallengeId())
                .orElseThrow(() -> new HealBitsException(CHALLENGE_NOT_FOUND));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        validateParticipateChallenge(challenge, member, request.getParticipationFee());

        challenge.increaseParticipantsNum();

        useBalance(request.getParticipationFee(), email);

        return TakeChallengeDto.fromEntity(
                saveAndGetTakeChallenge(challenge, member, request.getParticipationFee())
        );

    }

    private void useBalance(long participationFee, String email) {
        try {
            TransactionDto transactionDto = transactionService.useBalance(new UseBalance.Request(participationFee), email);
            log.info("Succeed to Use Balance {} ", transactionDto);
        } catch (Exception e) {
            log.error("Failed to Use Balance By {} ", e.toString());
            transactionService.saveFailedUseBalance(
                    new UseBalance.Request(participationFee),
                    email
            );
            throw e;
        }
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

    public List<TakeChallengeInfo> getTookChallengeListByChallengeStatus(
            ChallengeStatus challengeStatus, String email
    ) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        return getTakeChallengeInfoList(challengeStatus, member);
    }

    private List<TakeChallengeInfo> getTakeChallengeInfoList(ChallengeStatus challengeStatus, Member member) {
        if (Objects.equals(challengeStatus, ChallengeStatus.READY)) {
            return takeChallengeRepository
                    .findAllByParticipatedMemberIdAndReady(member.getId(), LocalDate.now());
        } else if (Objects.equals(challengeStatus, ChallengeStatus.IN_PROGRESS)) {
            return takeChallengeRepository
                    .findAllByParticipatedMemberIdAndInProgress(member.getId(), LocalDate.now());
        } else if (Objects.equals(challengeStatus, ChallengeStatus.CLOSED)) {
            return takeChallengeRepository
                    .findAllByParticipatedMemberIdAndClosed(member.getId(), LocalDate.now());
        } else {
            throw new HealBitsException(CHALLENGE_STATUS_NOT_FOUND);
        }
    }

    @Transactional
    public CompleteTookChallenge.Response completeTookChallenge(long takeChallengeId, String email) {
        TakeChallenge takeChallenge = takeChallengeRepository.findById(takeChallengeId)
                .orElseThrow(() -> new HealBitsException(TAKE_CHALLENGE_NOT_FOUND));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        double verificationRate = getVerificationRate(takeChallenge);
        long paybackFee = getPaybackFee(takeChallenge, (int) verificationRate);

        validateCompleteTookChallenge(takeChallenge, verificationRate, paybackFee);

        paybackBalance(paybackFee, member);

        return CompleteTookChallenge.Response.builder()
                .challengeName(takeChallenge.getParticipatedChallenge().getChallengeName())
                .verificationRate(String.format("%.2f%%", verificationRate))
                .paybackFee(paybackFee)
                .build();
    }

    private void validateCompleteTookChallenge(
            TakeChallenge takeChallenge
            , double verificationRate
            , long paybackFee
    ) {
        if (LocalDate.now().isBefore(takeChallenge.getParticipatedChallenge().getEndDate())) {
            throw new HealBitsException(CHALLENGE_NOT_FINISHED);
        }

        if (LocalDate.now().isAfter(takeChallenge.getParticipatedChallenge().getEndDate())) {
            throw new HealBitsException(ALREADY_COMPLETED_CHALLENGE);
        }

        if (verificationRate < 0) {
            throw new HealBitsException(INVALID_VERIFICATION_RATE);
        }

        if (paybackFee < 0) {
            throw new HealBitsException(INVALID_PAYBACK_FEE);
        }
    }

    /**
     * 인증 퍼센트에 따라 지급
     * 100% 완료 시 -> 참가비의 105% 반환
     * 80% 이상 완료 시 -> 참가비의 100% 반환
     * 80% 미만 완료 시 -> 참가비의 90% 반환
     *
     * @param takeChallenge
     * @param verificationRate
     * @return
     */
    private static long getPaybackFee(TakeChallenge takeChallenge, int verificationRate) {
        long paybackFee = 0;

        if (verificationRate == PaybackPercentagePolicy.PERFECT) {
            paybackFee = (long) (takeChallenge.getParticipationFee()
                    + takeChallenge.getParticipationFee() * PaybackPercentagePolicy.PAYBACK_PERFECT);
        } else if (verificationRate >= PaybackPercentagePolicy.EXCELLENT) {
            paybackFee = takeChallenge.getParticipationFee();
        } else {
            paybackFee = (long) (takeChallenge.getParticipationFee() * PaybackPercentagePolicy.PAYBACK_BAD);
        }

        return paybackFee;
    }

    private double getVerificationRate(TakeChallenge takeChallenge) {
        List<Verification> verificationList = verificationRepository
                .findAllByVerifiedTakeChallengeId(takeChallenge.getId());

        LocalDate startDate = takeChallenge.getParticipatedChallenge().getStartDate();
        LocalDate endDate = takeChallenge.getParticipatedChallenge().getEndDate();

        int totalCnt = Period.between(startDate, endDate).getDays() + 1;
        int verifiedCnt = verificationList.size();

        return ((double) verifiedCnt / (double) totalCnt) * 100;
    }

    private void paybackBalance(long paybackFee, Member member) {
        try {
            TransactionDto transactionDto = transactionService.paybackBalance(paybackFee, member);
            log.info("Succeed to payback Balance {} ", transactionDto);
        } catch (Exception e) {
            log.error("Failed to payback Balance By {} ", e.toString());
            transactionService.saveFailedPaybackBalance(
                    paybackFee,
                    member
            );
            throw e;
        }
    }
}
