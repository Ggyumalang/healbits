package com.zerobase.healbits.service;

import com.zerobase.healbits.challenge.domain.Challenge;
import com.zerobase.healbits.challenge.repository.ChallengeRepository;
import com.zerobase.healbits.exception.HealBitsException;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.member.repository.MemberRepository;
import com.zerobase.healbits.takechallenge.domain.TakeChallenge;
import com.zerobase.healbits.takechallenge.dto.ParticipateChallenge;
import com.zerobase.healbits.takechallenge.dto.TakeChallengeDto;
import com.zerobase.healbits.takechallenge.repository.TakeChallengeRepository;
import com.zerobase.healbits.takechallenge.service.TakeChallengeService;
import com.zerobase.healbits.type.ChallengeCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.zerobase.healbits.type.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TakeChallengeServiceTest {

    @Mock
    private TakeChallengeRepository takeChallengeRepository;

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TakeChallengeService takeChallengeService;



    @Test
    void success_participateChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .balance(2000)
                .build();

        Challenge challenge = Challenge.builder()
                .id(1L)
                .registeredMember(member)
                .challengeName("challenge2")
                .challengeCategory(ChallengeCategory.HEALTH)
                .summary("abc")
                .contents("abcdef")
                .participantsNum(0)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        given(challengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(challenge));

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        given(takeChallengeRepository.save(any()))
                .willReturn(TakeChallenge.builder()
                        .participatedChallenge(challenge)
                        .participatedMember(member)
                        .participationFee(2000)
                        .build());
        ArgumentCaptor<TakeChallenge> captor = ArgumentCaptor.forClass(TakeChallenge.class);
        //when 어떤 경우에
//        TakeChallengeDto takeChallengeDto = takeChallengeService
//                .participateChallenge(new ParticipateChallenge.Request(
//                        1, 1000
//                ), "abc@1234");
        //then 이런 결과가 나온다.
        verify(takeChallengeRepository, times(1)).save(captor.capture());
        assertEquals("challenge2", captor.getValue().getParticipatedChallenge().getChallengeName());
        assertEquals(1000, captor.getValue().getParticipationFee());
        assertEquals("khg1111@naver.com", captor.getValue().getParticipatedMember().getEmail());
        assertEquals(1, captor.getValue().getParticipatedChallenge().getParticipantsNum());
//        assertEquals("khg1111@naver.com", takeChallengeDto.getEmail());
//        assertEquals(2000, takeChallengeDto.getParticipationFee());
//        assertEquals("challenge2", takeChallengeDto.getChallengeName());
    }

    @Test
    @DisplayName("CHALLENGE_NOT_FOUND_participateChallenge")
    void CHALLENGE_NOT_FOUND_participateChallenge() {
        //given 어떤 데이터가 주어졌을 때
        given(challengeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when 어떤 경우에
//        HealBitsException healBitsException = assertThrows(HealBitsException.class, () -> takeChallengeService
//                .participateChallenge(new ParticipateChallenge.Request(
//                        1, 1000
//                ), "abc@1234"));
        //then 이런 결과가 나온다.
//        assertEquals(CHALLENGE_NOT_FOUND , healBitsException.getErrorCode());

    }

    @Test
    @DisplayName("EMAIL_NOT_FOUND_participateChallenge")
    void EMAIL_NOT_FOUND_participateChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Challenge challenge = Challenge.builder()
                .id(1L)
                .challengeName("challenge2")
                .challengeCategory(ChallengeCategory.HEALTH)
                .summary("abc")
                .contents("abcdef")
                .participantsNum(0)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        given(challengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(challenge));

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        //when 어떤 경우에
//        HealBitsException healBitsException = assertThrows(HealBitsException.class, () -> takeChallengeService
//                .participateChallenge(new ParticipateChallenge.Request(
//                        1, 1000
//                ), "abc@1234"));
//        //then 이런 결과가 나온다.
//        assertEquals(EMAIL_NOT_FOUND , healBitsException.getErrorCode());
    }

    @Test
    @DisplayName("ALREADY_PARTICIPATED_CHALLENGE_participateChallenge")
    void ALREADY_PARTICIPATED_CHALLENGE_participateChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .balance(2000)
                .build();

        Challenge challenge = Challenge.builder()
                .id(1L)
                .registeredMember(member)
                .challengeName("challenge2")
                .challengeCategory(ChallengeCategory.HEALTH)
                .summary("abc")
                .contents("abcdef")
                .participantsNum(0)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        given(challengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(challenge));

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        given(takeChallengeRepository.existsByParticipatedChallengeIdAndParticipatedMemberId(anyLong(), anyLong()))
                .willReturn(true);
        //when 어떤 경우에
//        HealBitsException healBitsException = assertThrows(HealBitsException.class, () -> takeChallengeService
//                .participateChallenge(new ParticipateChallenge.Request(
//                        1, 1000
//                ), "abc@1234"));
//        //then 이런 결과가 나온다.
//        assertEquals(ALREADY_PARTICIPATED_CHALLENGE , healBitsException.getErrorCode());
    }

    @Test
    @DisplayName("INVALID_START_DATE_participateChallenge")
    void INVALID_START_DATE_participateChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .balance(2000)
                .build();

        Challenge challenge = Challenge.builder()
                .id(1L)
                .registeredMember(member)
                .challengeName("challenge2")
                .challengeCategory(ChallengeCategory.HEALTH)
                .summary("abc")
                .contents("abcdef")
                .participantsNum(0)
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        given(challengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(challenge));

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        given(takeChallengeRepository.existsByParticipatedChallengeIdAndParticipatedMemberId(anyLong(), anyLong()))
                .willReturn(false);
        //when 어떤 경우에
//        HealBitsException healBitsException = assertThrows(HealBitsException.class, () -> takeChallengeService
//                .participateChallenge(new ParticipateChallenge.Request(
//                        1, 1000
//                ), "abc@1234"));
//        //then 이런 결과가 나온다.
//        assertEquals(INVALID_START_DATE , healBitsException.getErrorCode());
    }

    @Test
    @DisplayName("INVALID_AMOUNT_participateChallenge")
    void INVALID_AMOUNT_participateChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .balance(2000)
                .build();

        Challenge challenge = Challenge.builder()
                .id(1L)
                .registeredMember(member)
                .challengeName("challenge2")
                .challengeCategory(ChallengeCategory.HEALTH)
                .summary("abc")
                .contents("abcdef")
                .participantsNum(0)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .build();

        given(challengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(challenge));

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        given(takeChallengeRepository.existsByParticipatedChallengeIdAndParticipatedMemberId(anyLong(), anyLong()))
                .willReturn(false);
        //when 어떤 경우에
//        HealBitsException healBitsException = assertThrows(HealBitsException.class, () -> takeChallengeService
//                .participateChallenge(new ParticipateChallenge.Request(
//                        1, -1
//                ), "abc@1234"));
//        //then 이런 결과가 나온다.
//        assertEquals(INVALID_AMOUNT , healBitsException.getErrorCode());
    }

}