package com.zerobase.healbits.transaction.service;

import com.zerobase.healbits.common.exception.HealBitsException;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.member.repository.MemberRepository;
import com.zerobase.healbits.transaction.domain.Transaction;
import com.zerobase.healbits.transaction.dto.ChargeBalance;
import com.zerobase.healbits.transaction.dto.TransactionDto;
import com.zerobase.healbits.transaction.dto.UseBalance;
import com.zerobase.healbits.transaction.repository.TransactionRepository;
import com.zerobase.healbits.common.type.TransactionResultType;
import com.zerobase.healbits.common.type.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.zerobase.healbits.common.type.ErrorCode.EMAIL_NOT_FOUND;
import static com.zerobase.healbits.common.type.TransactionResultType.FAIL;
import static com.zerobase.healbits.common.type.TransactionResultType.SUCCESS;
import static com.zerobase.healbits.common.type.TransactionType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionDto useBalance(UseBalance.Request request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        member.useBalance(request.getParticipationFee());

        return TransactionDto.fromEntity(
                saveAndGetTransaction(request.getParticipationFee(), member, USE, SUCCESS)
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedUseBalance(UseBalance.Request request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        saveAndGetTransaction(request.getParticipationFee(), member, USE, FAIL);
    }

    @Transactional
    public TransactionDto paybackBalance(long paybackFee, Member member) {

        member.chargeBalance(paybackFee);

        return TransactionDto.fromEntity(
                saveAndGetTransaction(paybackFee, member, PAYBACK, SUCCESS)
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedPaybackBalance(long paybackFee, Member member) {
        saveAndGetTransaction(paybackFee, member, PAYBACK, FAIL);
    }

    @Transactional
    public TransactionDto chargeBalance(ChargeBalance.Request request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        member.chargeBalance(request.getChargeAmount());
        return TransactionDto.fromEntity(
                saveAndGetTransaction(request.getChargeAmount(), member, CHARGE, SUCCESS)
        );
    }

    @Transactional
    public void saveFailedChargeBalance(ChargeBalance.Request request, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        saveAndGetTransaction(request.getChargeAmount(), member, CHARGE, FAIL);
    }

    private Transaction saveAndGetTransaction(
            long amount
            , Member member
            , TransactionType transactionType
            , TransactionResultType transactionResultType
    ) {
        return transactionRepository.save(Transaction.builder()
                .member(member)
                .transactionType(transactionType)
                .transactionResultType(transactionResultType)
                .transactionId(UUID.randomUUID().toString().replace("-", ""))
                .amount(amount)
                .balanceSnapshot(member.getBalance())
                .transactedDateTime(LocalDateTime.now())
                .build());
    }


}
