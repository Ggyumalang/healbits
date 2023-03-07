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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.zerobase.healbits.type.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ChallengeRepository challengeRepository;

    @InjectMocks
    private ChallengeService challengeService;

    @Test
    void success_registerChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .build();
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        given(challengeRepository.save(any()))
                .willReturn(Challenge.builder()
                        .id(1L)
                        .member(member)
                        .challengeName("challenge2")
                        .challengeCategory(ChallengeCategory.HEALTH)
                        .summary("abc")
                        .contents("abcdef")
                        .participantsNum(0)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(7))
                        .build());
        //when 어떤 경우에
        ChallengeDto challengeDto = challengeService.registerChallenge(new RegisterChallenge.Request(
                "abc@naver.com",
                "challenge",
                "LIFE",
                "요약입니다",
                "내용입니다",
                LocalDate.now(),
                LocalDate.now().plusDays(14)
        ));
        ArgumentCaptor<Challenge> captor = ArgumentCaptor.forClass(Challenge.class);
        //then 이런 결과가 나온다.
        verify(challengeRepository, times(1)).save(captor.capture());
        assertEquals("challenge", captor.getValue().getChallengeName());
        assertEquals(ChallengeCategory.LIFE, captor.getValue().getChallengeCategory());
        assertEquals("요약입니다", captor.getValue().getSummary());
        assertEquals("내용입니다", captor.getValue().getContents());
        assertEquals(LocalDate.now(), captor.getValue().getStartDate());
        assertEquals(LocalDate.now().plusDays(14), captor.getValue().getEndDate());
        assertEquals("khg1111@naver.com", challengeDto.getEmail());
        assertEquals("challenge2", challengeDto.getChallengeName());
        assertEquals(ChallengeCategory.HEALTH, challengeDto.getChallengeCategory());
        assertEquals("abc", challengeDto.getSummary());
        assertEquals(LocalDate.now(), challengeDto.getStartDate());
        assertEquals(LocalDate.now().plusDays(7), challengeDto.getEndDate());
    }

    @Test
    @DisplayName("Email_Not_Found_RegisterChallenge")
    void email_Not_Found_RegisterChallenge() {
        //given 어떤 데이터가 주어졌을 때
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> challengeService.registerChallenge(new RegisterChallenge.Request(
                        "abc@naver.com",
                        "challenge",
                        "LIFE",
                        "요약입니다",
                        "내용입니다",
                        LocalDate.now(),
                        LocalDate.now().plusDays(14)
                )));

        //then
        assertEquals(EMAIL_NOT_FOUND, healBitsException.getErrorCode());
    }

    @Test
    @DisplayName("CHALLENGE_CATEGORY_NOT_FOUND_RegisterChallenge")
    void challenge_Category_Not_Found_RegisterChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .build();
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> challengeService.registerChallenge(new RegisterChallenge.Request(
                        "abc@naver.com",
                        "challenge",
                        "LIF",
                        "요약입니다",
                        "내용입니다",
                        LocalDate.now(),
                        LocalDate.now().plusDays(14)
                )));

        //then
        assertEquals(CHALLENGE_CATEGORY_NOT_FOUND, healBitsException.getErrorCode());
    }

    @Test
    @DisplayName("END_DATE_INVALID_RegisterChallenge")
    void end_Date_Invalid_RegisterChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .build();
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> challengeService.registerChallenge(new RegisterChallenge.Request(
                        "abc@naver.com",
                        "challenge",
                        "LIFE",
                        "요약입니다",
                        "내용입니다",
                        LocalDate.now(),
                        LocalDate.now().minusDays(14)
                )));

        //then
        assertEquals(END_DATE_INVALID, healBitsException.getErrorCode());
    }

    @Test
    @DisplayName("START_DATE_INVALID_RegisterChallenge")
    void start_Date_Invalid_RegisterChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .build();
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> challengeService.registerChallenge(new RegisterChallenge.Request(
                        "abc@naver.com",
                        "challenge",
                        "LIFE",
                        "요약입니다",
                        "내용입니다",
                        LocalDate.now().minusDays(1),
                        LocalDate.now()
                )));

        //then
        assertEquals(START_DATE_INVALID, healBitsException.getErrorCode());
    }

    @Test
    void success_getChallengeListByCategory() {
        //given 어떤 데이터가 주어졌을 때

        given(challengeRepository.findAllByChallengeCategoryAndEndDateGreaterThanEqual(any(),any()))
                .willReturn(List.of(Challenge.builder()
                        .challengeName("challenge2")
                        .challengeCategory(ChallengeCategory.HEALTH)
                        .summary("abc")
                        .participantsNum(3000)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(7))
                        .build()));
        //when 어떤 경우에
        List<ChallengeSummaryInfo> list = challengeService.getChallengeListByCategory("HEALTH");
        //then 이런 결과가 나온다.
        assertEquals("challenge2", list.get(0).getChallengeName());
        assertEquals(ChallengeCategory.HEALTH, list.get(0).getChallengeCategory());
        assertEquals("abc", list.get(0).getSummary());
        assertEquals(3000, list.get(0).getParticipantsNum());
        assertEquals(8, list.get(0).getChallengeDuration());
        assertEquals(0, list.get(0).getRemainingDaysToStart());
    }

    @Test
    @DisplayName("CHALLENGE_CATEGORY_NOT_FOUND_GetChallengeListByCategory")
    void challenge_Category_Not_Found_GetChallengeListByCategory() {
        //given 어떤 데이터가 주어졌을 때
        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> challengeService.getChallengeListByCategory("LIF")
        );

        //then
        assertEquals(CHALLENGE_CATEGORY_NOT_FOUND, healBitsException.getErrorCode());
    }

    @Test
    void success_getChallengeDetail() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .build();

        given(challengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(Challenge.builder()
                        .id(1L)
                        .member(member)
                        .challengeName("challenge")
                        .challengeCategory(ChallengeCategory.HEALTH)
                        .contents("abcdef")
                        .participantsNum(0)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(7))
                        .build()));
        //when 어떤 경우에
        ChallengeDto challengeDto = challengeService.getChallengeDetail(2);
        //then 이런 결과가 나온다.
        assertEquals("challenge", challengeDto.getChallengeName());
        assertEquals(ChallengeCategory.HEALTH, challengeDto.getChallengeCategory());
        assertEquals("abcdef", challengeDto.getContents());
        assertEquals(LocalDate.now(), challengeDto.getStartDate());
        assertEquals(LocalDate.now().plusDays(7), challengeDto.getEndDate());
    }

    @Test
    @DisplayName("CHALLENGE_NOT_FOUND_GetChallengeDetail")
    void challenge_Not_Found_GetChallengeDetail() {
        //given 어떤 데이터가 주어졌을 때
        given(challengeRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class
                , () -> challengeService.getChallengeDetail(2));
        //then 이런 결과가 나온다.
        assertEquals(CHALLENGE_NOT_FOUND, healBitsException.getErrorCode());
    }
}