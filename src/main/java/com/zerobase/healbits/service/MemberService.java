package com.zerobase.healbits.service;

import com.zerobase.healbits.domain.Member;
import com.zerobase.healbits.dto.MemberDto;
import com.zerobase.healbits.dto.MemberInfo;
import com.zerobase.healbits.dto.RegisterMember;
import com.zerobase.healbits.dto.TokenInfo;
import com.zerobase.healbits.exception.HealBitsException;
import com.zerobase.healbits.jwt.JwtTokenProvider;
import com.zerobase.healbits.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static com.zerobase.healbits.type.ErrorCode.EMAIL_ALREADY_EXIST;
import static com.zerobase.healbits.type.ErrorCode.EMAIL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDto registerMember(RegisterMember.Request request) {

        checkExistedEmail(request.getEmail());

        return MemberDto.fromEntity(memberRepository.save(Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .build()));
    }


    private void checkExistedEmail(@NotBlank String email) {
        memberRepository.findByEmail(email).ifPresent(it -> {
            throw new HealBitsException(EMAIL_ALREADY_EXIST);
        });
    }

    public TokenInfo login(String email, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 loadUserByUsername 메서드가 실행

        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성하고 리턴
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND));

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(member.getEmail()
                , member.getPassword()
                , grantedAuthorities);
    }

    public MemberInfo getMemberInfo(String email) {
        return MemberInfo.fromEntity(memberRepository.findByEmail(email)
                .orElseThrow(() -> new HealBitsException(EMAIL_NOT_FOUND)));
    }

}
