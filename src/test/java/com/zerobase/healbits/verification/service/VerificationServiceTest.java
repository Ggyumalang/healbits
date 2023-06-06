//TODO AWS S3 TEST 방법 확인 후 다시 진행
package com.zerobase.healbits.verification.service;

import com.zerobase.healbits.awss3.AwsS3Api;
import com.zerobase.healbits.challenge.domain.Challenge;
import com.zerobase.healbits.common.exception.HealBitsException;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.takechallenge.domain.TakeChallenge;
import com.zerobase.healbits.takechallenge.repository.TakeChallengeRepository;
import com.zerobase.healbits.challenge.type.ChallengeCategory;
import com.zerobase.healbits.verification.domain.Verification;
import com.zerobase.healbits.verification.dto.VerificationDto;
import com.zerobase.healbits.verification.dto.VerificationInfo;
import com.zerobase.healbits.verification.respository.VerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.zerobase.healbits.common.type.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {

    @Mock
    private TakeChallengeRepository takeChallengeRepository;

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private AwsS3Api awsS3Api;

    @InjectMocks
    private VerificationService verificationService;

    @Test
    void success_verifyChallenge() {
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
                .challengeName("challenge")
                .challengeCategory(ChallengeCategory.HEALTH)
                .summary("abc")
                .contents("abcdef")
                .participantsNum(0)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .build();

        TakeChallenge takeChallenge = TakeChallenge.builder()
                .id(2)
                .participatedMember(member)
                .participatedChallenge(challenge)
                .participationFee(1000)
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile",
                "health.jpg",
                "image/jpg",
                "<<jpeg data>>".getBytes());

        given(takeChallengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(takeChallenge));

        given(verificationRepository.save(any()))
                .willReturn(Verification.builder()
                        .verifiedTakeChallenge(takeChallenge)
                        .verifiedDate(LocalDate.now())
                        .imagePath("/image")
                        .build());

        ArgumentCaptor<Verification> captor = ArgumentCaptor.forClass(Verification.class);
        //when 어떤 경우에
        VerificationDto verificationDto = verificationService.verifyChallenge(1
                , multipartFile
                , "khg1111@naver.com");
        //then 이런 결과가 나온다.
        verify(verificationRepository, times(1)).save(captor.capture());
        assertEquals(LocalDate.now(), captor.getValue().getVerifiedDate());
        assertEquals(1000, captor.getValue().getVerifiedTakeChallenge().getParticipationFee());
        assertEquals("/image", verificationDto.getImagePath());
        assertEquals(LocalDate.now(), verificationDto.getVerifiedDate());
        assertEquals("challenge", verificationDto.getChallengeName());
        assertEquals("khg1111@naver.com", verificationDto.getEmail());
    }

    @Test
    @DisplayName("TAKE_CHALLENGE_NOT_FOUND_verifyChallenge")
    void TAKE_CHALLENGE_NOT_FOUND_verifyChallenge() {
        //given 어떤 데이터가 주어졌을 때
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile",
                "health.jpg",
                "image/jpg",
                "<<jpeg data>>".getBytes());

        given(takeChallengeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> verificationService.verifyChallenge(
                        1
                        , multipartFile
                        , "abcd@naver.com"));
        //then 이런 결과가 나온다.
        assertEquals(TAKE_CHALLENGE_NOT_FOUND, healBitsException.getErrorCode());
    }

    @Test
    @DisplayName("VERIFICATION_USER_NOT_MATCHED_verifyChallenge")
    void VERIFICATION_USER_NOT_MATCHED_verifyChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .balance(2000)
                .build();

        TakeChallenge takeChallenge = TakeChallenge.builder()
                .id(2)
                .participatedMember(member)
                .participationFee(1000)
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile",
                "health.jpg",
                "image/jpg",
                "<<jpeg data>>".getBytes());

        given(takeChallengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(takeChallenge));

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> verificationService.verifyChallenge(
                        1
                        , multipartFile
                        , "abcd@naver.com"));
        //then 이런 결과가 나온다.
        assertEquals(VERIFICATION_USER_NOT_MATCHED, healBitsException.getErrorCode());
    }

    @Test
    @DisplayName("FILE_NOT_EXIST_verifyChallenge")
    void FILE_NOT_EXIST_verifyChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .balance(2000)
                .build();

        TakeChallenge takeChallenge = TakeChallenge.builder()
                .id(2)
                .participatedMember(member)
                .participationFee(1000)
                .build();

        given(takeChallengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(takeChallenge));

        //when 어떤 경우에
        NullPointerException nullPointerException = assertThrows(NullPointerException.class,
                () -> verificationService.verifyChallenge(
                        1
                        , null
                        , "khg1111@naver.com"));
        //then 이런 결과가 나온다.
        assertNull(nullPointerException.getMessage());
    }

    @Test
    @DisplayName("VERIFICATION_ALREADY_EXIST_verifyChallenge")
    void VERIFICATION_ALREADY_EXIST_verifyChallenge() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .balance(2000)
                .build();

        TakeChallenge takeChallenge = TakeChallenge.builder()
                .id(2)
                .participatedMember(member)
                .participationFee(1000)
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile",
                "health.jpg",
                "image/jpg",
                "<<jpeg data>>".getBytes());

        given(takeChallengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(takeChallenge));

        given(verificationRepository.existsByVerifiedTakeChallengeIdAndVerifiedDate(anyLong(), any()))
                .willReturn(true);

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> verificationService.verifyChallenge(
                        1
                        , multipartFile
                        , "khg1111@naver.com"));
        //then 이런 결과가 나온다.
        assertEquals(VERIFICATION_ALREADY_EXIST, healBitsException.getErrorCode());
    }

    @Test
    @DisplayName("CHALLENGE_NOT_STARTED_verifyChallenge")
    void CHALLENGE_NOT_STARTED_verifyChallenge() {
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
                .startDate(LocalDate.now().plusDays(1))
                .build();

        TakeChallenge takeChallenge = TakeChallenge.builder()
                .id(2)
                .participatedMember(member)
                .participatedChallenge(challenge)
                .participationFee(1000)
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile",
                "health.jpg",
                "image/jpg",
                "<<jpeg data>>".getBytes());

        given(takeChallengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(takeChallenge));

        given(verificationRepository.existsByVerifiedTakeChallengeIdAndVerifiedDate(anyLong(), any()))
                .willReturn(false);

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> verificationService.verifyChallenge(
                        1
                        , multipartFile
                        , "khg1111@naver.com"));
        //then 이런 결과가 나온다.
        assertEquals(CHALLENGE_NOT_STARTED, healBitsException.getErrorCode());
    }

    @Test
    @DisplayName("ALREADY_FINISHED_CHALLENGE_verifyChallenge")
    void ALREADY_FINISHED_CHALLENGE_verifyChallenge() {
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
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().minusDays(1))
                .build();

        TakeChallenge takeChallenge = TakeChallenge.builder()
                .id(2)
                .participatedMember(member)
                .participatedChallenge(challenge)
                .participationFee(1000)
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile",
                "health.jpg",
                "image/jpg",
                "<<jpeg data>>".getBytes());

        given(takeChallengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(takeChallenge));

        given(verificationRepository.existsByVerifiedTakeChallengeIdAndVerifiedDate(anyLong(), any()))
                .willReturn(false);

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> verificationService.verifyChallenge(
                        1
                        , multipartFile
                        , "khg1111@naver.com"));
        //then 이런 결과가 나온다.
        assertEquals(ALREADY_FINISHED_CHALLENGE, healBitsException.getErrorCode());
    }

    @Test
    void success_getVerificationInfo() {
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
                .challengeName("challenge")
                .challengeCategory(ChallengeCategory.HEALTH)
                .summary("abc")
                .contents("abcdef")
                .participantsNum(0)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .build();

        TakeChallenge takeChallenge = TakeChallenge.builder()
                .id(2)
                .participatedMember(member)
                .participatedChallenge(challenge)
                .participationFee(1000)
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile",
                "health.jpg",
                "image/jpg",
                "<<jpeg data>>".getBytes());

        given(takeChallengeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(takeChallenge));

        given(verificationRepository.findAllByVerifiedTakeChallengeId(anyLong()))
                .willReturn(List.of(Verification.builder()
                        .verifiedTakeChallenge(takeChallenge)
                        .imagePath("/image")
                        .verifiedDate(LocalDate.now())
                        .build()));

        //when 어떤 경우에
        VerificationInfo verificationInfo = verificationService.getVerificationInfo(1);
        //then 이런 결과가 나온다.
        assertEquals(1, verificationInfo.getVerifiedCnt());
        assertEquals(7, verificationInfo.getRemainVerificationCnt());
        assertEquals(String.format("%.2f%%", 1.0 / 8.0 * 100), verificationInfo.getVerificationRate());
    }

    @Test
    @DisplayName("TAKE_CHALLENGE_NOT_FOUND_getVerificationInfo")
    void TAKE_CHALLENGE_NOT_FOUND_getVerificationInfo() {
        //given 어떤 데이터가 주어졌을 때
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile",
                "health.jpg",
                "image/jpg",
                "<<jpeg data>>".getBytes());

        given(takeChallengeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when 어떤 경우에
        HealBitsException healBitsException = assertThrows(HealBitsException.class,
                () -> verificationService.verifyChallenge(
                        1
                        , multipartFile
                        , "abcd@naver.com"));
        //then 이런 결과가 나온다.
        assertEquals(TAKE_CHALLENGE_NOT_FOUND, healBitsException.getErrorCode());
    }

}