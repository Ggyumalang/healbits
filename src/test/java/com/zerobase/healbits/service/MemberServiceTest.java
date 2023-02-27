package com.zerobase.healbits.service;

import com.zerobase.healbits.domain.Member;
import com.zerobase.healbits.dto.MemberDto;
import com.zerobase.healbits.dto.RegisterMember;
import com.zerobase.healbits.exception.HealBitsException;
import com.zerobase.healbits.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zerobase.healbits.type.ErrorCode.EMAIL_ALREADY_EXIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void success_RegisterMember() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .balance(10000)
                .build();
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        given(memberRepository.save(any()))
                .willReturn(member);
        //when 어떤 경우에
        MemberDto memberDto = memberService.registerMember(new RegisterMember.Request(
                "khg2222@hanmail.net",
                "1111",
                "김",
                "01022223333",
                20000
        ));
        ArgumentCaptor<Member> argumentCaptor = ArgumentCaptor.forClass(Member.class);
        //then 이런 결과가 나온다.
        verify(memberRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(20000, argumentCaptor.getValue().getBalance());
        assertEquals(10000, memberDto.getBalance());
    }

    @Test
    @DisplayName("RegisterMember_Fail_EMAIL_ALREADY_EXIST")
    void fail_RegisterMember() {
        //given 어떤 데이터가 주어졌을 때
        Member member = Member.builder()
                .email("khg1111@naver.com")
                .password("1234")
                .name("홍길")
                .phone("01011112222")
                .balance(10000)
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(member));
        //when 어떤 경우에
        HealBitsException exception = assertThrows(HealBitsException.class, () -> memberService.registerMember(new RegisterMember.Request(
                "khg2222@hanmail.net",
                "1111",
                "김",
                "01022223333",
                20000
        )));
        //then 이런 결과가 나온다.
        assertEquals(EMAIL_ALREADY_EXIST, exception.getErrorCode());
    }

}