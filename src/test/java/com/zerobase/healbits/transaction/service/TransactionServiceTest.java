package com.zerobase.healbits.transaction.service;

import com.zerobase.healbits.exception.HealBitsException;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.member.repository.MemberRepository;
import com.zerobase.healbits.transaction.domain.Transaction;
import com.zerobase.healbits.transaction.dto.ChargeBalance;
import com.zerobase.healbits.transaction.dto.TransactionDto;
import com.zerobase.healbits.transaction.dto.UseBalance;
import com.zerobase.healbits.transaction.repository.TransactionRepository;
import com.zerobase.healbits.type.TransactionResultType;
import com.zerobase.healbits.type.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.zerobase.healbits.type.ErrorCode.EMAIL_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @WithMockUser
    void success_useBalance() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .balance(1500)
                .name("홍길")
                .phone("01011112222")
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        given(transactionRepository.save(any()))
                .willReturn(Transaction.builder()
                        .member(member)
                        .transactionType(TransactionType.USE)
                        .transactionResultType(TransactionResultType.SUCCESS)
                        .transactionId("1")
                        .amount(1000)
                        .balanceSnapshot(2000)
                        .transactedDateTime(LocalDateTime.now())
                        .build());

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        //when 어떤 경우에
        TransactionDto transactionDto = transactionService
                .useBalance(new UseBalance.Request(500), "abcd@1234");

        //then 이런 결과가 나온다.
        verify(transactionRepository, times(1)).save(captor.capture());
        assertEquals(500, captor.getValue().getAmount());
        assertEquals(1000, captor.getValue().getBalanceSnapshot());
        assertEquals("khg1111@naver.com", captor.getValue().getMember().getEmail());
        assertEquals("1", transactionDto.getTransactionId());
        assertEquals("khg1111@naver.com", transactionDto.getEmail());
        assertEquals(2000, transactionDto.getBalanceSnapshot());
        assertEquals(TransactionType.USE, transactionDto.getTransactionType());
        assertEquals(TransactionResultType.SUCCESS, transactionDto.getTransactionResultType());
    }

    @Test
    @DisplayName("EMAIL_NOT_FOUND_useBalance")
    void EMAIL_NOT_FOUND_useBalance() {
        //given 어떤 데이터가 주어졌을 때
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class, () -> transactionService
                .useBalance(new UseBalance.Request(500), "abcd@1234"));

        //then 이런 결과가 나온다.
        assertEquals(EMAIL_NOT_FOUND, healBitsException.getErrorCode());
    }

    @Test
    void success_saveFailedUseBalance() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .balance(1500)
                .name("홍길")
                .phone("01011112222")
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        given(transactionRepository.save(any()))
                .willReturn(Transaction.builder()
                        .member(member)
                        .transactionType(TransactionType.USE)
                        .transactionResultType(TransactionResultType.FAIL)
                        .transactionId("1")
                        .amount(1000)
                        .balanceSnapshot(2000)
                        .transactedDateTime(LocalDateTime.now())
                        .build());

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        //when 어떤 경우에
        TransactionDto transactionDto = transactionService.useBalance(new UseBalance.Request(500), "abcd@1234");

        //then 이런 결과가 나온다.
        verify(transactionRepository, times(1)).save(captor.capture());
        assertEquals(500, captor.getValue().getAmount());
        assertEquals(1000, captor.getValue().getBalanceSnapshot());
        assertEquals("khg1111@naver.com", captor.getValue().getMember().getEmail());
        assertEquals(TransactionType.USE, captor.getValue().getTransactionType());
        assertEquals(TransactionResultType.FAIL, transactionDto.getTransactionResultType());
    }

    @Test
    void success_chargeBalance() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .balance(1500)
                .name("홍길")
                .phone("01011112222")
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        given(transactionRepository.save(any()))
                .willReturn(Transaction.builder()
                        .member(member)
                        .transactionType(TransactionType.CHARGE)
                        .transactionResultType(TransactionResultType.SUCCESS)
                        .transactionId("1")
                        .amount(1000)
                        .balanceSnapshot(3000)
                        .transactedDateTime(LocalDateTime.now())
                        .build());

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        //when 어떤 경우에
        TransactionDto transactionDto = transactionService
                .chargeBalance(new ChargeBalance.Request(500), "abc@1234");

        //then 이런 결과가 나온다.
        verify(transactionRepository, times(1)).save(captor.capture());
        assertEquals(500, captor.getValue().getAmount());
        assertEquals(2000, captor.getValue().getBalanceSnapshot());
        assertEquals("khg1111@naver.com", captor.getValue().getMember().getEmail());
        assertEquals("1", transactionDto.getTransactionId());
        assertEquals(1000, transactionDto.getAmount());
        assertEquals(3000, transactionDto.getBalanceSnapshot());
        assertEquals(TransactionType.CHARGE, transactionDto.getTransactionType());
        assertEquals(TransactionResultType.SUCCESS, transactionDto.getTransactionResultType());
    }

    @Test
    @DisplayName("EMAIL_NOT_FOUND_chargeBalance")
    void EMAIL_NOT_FOUND_chargeBalance() {
        //given 어떤 데이터가 주어졌을 때
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> transactionService.chargeBalance(
                        new ChargeBalance.Request(500), "abc@1234")
        );

        //then 이런 결과가 나온다.
        assertEquals(EMAIL_NOT_FOUND, healBitsException.getErrorCode());
    }

    @Test
    void success_saveFailedChargeBalance() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .balance(1500)
                .name("홍길")
                .phone("01011112222")
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        given(transactionRepository.save(any()))
                .willReturn(Transaction.builder()
                        .member(member)
                        .transactionType(TransactionType.USE)
                        .transactionResultType(TransactionResultType.FAIL)
                        .transactionId("1")
                        .amount(1000)
                        .balanceSnapshot(2000)
                        .transactedDateTime(LocalDateTime.now())
                        .build());

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        //when 어떤 경우에
        transactionService.saveFailedChargeBalance(new ChargeBalance.Request(500), "abc@1234");

        //then 이런 결과가 나온다.
        verify(transactionRepository, times(1)).save(captor.capture());
        assertEquals(500, captor.getValue().getAmount());
        assertEquals(1500, captor.getValue().getBalanceSnapshot());
        assertEquals("khg1111@naver.com", captor.getValue().getMember().getEmail());
        assertEquals(TransactionType.CHARGE, captor.getValue().getTransactionType());
        assertEquals(TransactionResultType.FAIL, captor.getValue().getTransactionResultType());
    }
}