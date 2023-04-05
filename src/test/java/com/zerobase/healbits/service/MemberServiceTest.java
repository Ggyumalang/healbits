package com.zerobase.healbits.service;

import com.zerobase.healbits.common.exception.HealBitsException;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.member.dto.MemberDto;
import com.zerobase.healbits.member.dto.MemberInfo;
import com.zerobase.healbits.member.dto.RegisterMember;
import com.zerobase.healbits.member.repository.MemberRepository;
import com.zerobase.healbits.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.zerobase.healbits.common.type.ErrorCode.EMAIL_ALREADY_EXIST;
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

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    void success_RegisterMember() {
        //given 어떤 데이터가 주어졌을 때
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        given(memberRepository.save(any()))
                .willReturn(Member.builder()
                        .email("khg1111@naver.com")
                        .password("1234")
                        .name("홍길")
                        .phone("01011112222")
                        .build());
        //when 어떤 경우에
        MemberDto memberDto = memberService.registerMember(new RegisterMember.Request(
                "khg2222@hanmail.net",
                "1111",
                "김",
                "01022223333"
        ));
        ArgumentCaptor<Member> argumentCaptor = ArgumentCaptor.forClass(Member.class);
        //then 이런 결과가 나온다.
        verify(memberRepository, times(1)).save(argumentCaptor.capture());
        assertEquals("01022223333", argumentCaptor.getValue().getPhone());
        assertEquals("01011112222", memberDto.getPhone());
        assertEquals("khg1111@naver.com", memberDto.getEmail());
        assertEquals("홍길", memberDto.getName());
    }

    @Test
    @DisplayName("RegisterMember_Fail_EMAIL_ALREADY_EXIST")
    void fail_RegisterMember() {
        //given 어떤 데이터가 주어졌을 때
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(Member.builder()
                        .email("khg1111@naver.com")
                        .password("1234")
                        .name("홍길")
                        .phone("01011112222")
                        .balance(10000)
                        .build()));
        //when 어떤 경우에
        HealBitsException exception = assertThrows(HealBitsException.class, () -> memberService.registerMember(new RegisterMember.Request(
                "khg2222@hanmail.net",
                "1111",
                "김",
                "01022223333"
        )));
        //then 이런 결과가 나온다.
        assertEquals(EMAIL_ALREADY_EXIST, exception.getErrorCode());
    }

    @Test
    void success_LoadUserByUsername() {
        //given 어떤 데이터가 주어졌을 때
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(Member.builder()
                        .email("khg1111@naver.com")
                        .password("1234")
                        .name("홍길")
                        .phone("01011112222")
                        .balance(10000)
                        .build()));
        //when 어떤 경우에
        UserDetails userDetails = memberService.loadUserByUsername("khg1111@hanmail.net");
        //then 이런 결과가 나온다.
        assertEquals("khg1111@naver.com", userDetails.getUsername());
        assertEquals("1234", userDetails.getPassword());
    }

    @Test
    @DisplayName("EMAIL_NOT_FOUND_LoadUserByUsername")
    void fail_LoadUserByUsername() {
        //given 어떤 데이터가 주어졌을 때
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        //when 어떤 경우에
        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> memberService.loadUserByUsername("khg1111@hanmail.net"));
        //then 이런 결과가 나온다.
        assertEquals("USER NOT FOUND -> khg1111@hanmail.net", usernameNotFoundException.getMessage());
    }

    @Test
    void success_getMemberInfo() {
        //given 어떤 데이터가 주어졌을 때
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(Member.builder()
                        .email("khg1111@naver.com")
                        .password("1234")
                        .name("홍길")
                        .phone("01011112222")
                        .balance(10000)
                        .build()));
        //when 어떤 경우에
        MemberInfo memberInfo = memberService.getMemberInfo("khg2154@hanmail.net");
        //then 이런 결과가 나온다.
        assertEquals("khg1111@naver.com", memberInfo.getEmail());
        assertEquals("홍길", memberInfo.getName());
        assertEquals("01011112222", memberInfo.getPhone());
        assertEquals(10000, memberInfo.getBalance());

    }

    @Test
    @DisplayName("EMAIL_NOT_FOUND_getMemberInfo")
    void fail_getMemberInfo() {
        //given 어떤 데이터가 주어졌을 때
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        //when 어떤 경우에
        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> memberService.loadUserByUsername("khg1111@hanmail.net"));
        //then 이런 결과가 나온다.
        assertEquals("USER NOT FOUND -> khg1111@hanmail.net", usernameNotFoundException.getMessage());
    }
}