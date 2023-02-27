package com.zerobase.healbits.service;

import com.zerobase.healbits.domain.Member;
import com.zerobase.healbits.dto.MemberDto;
import com.zerobase.healbits.dto.RegisterMember;
import com.zerobase.healbits.exception.HealBitsException;
import com.zerobase.healbits.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;

import static com.zerobase.healbits.type.ErrorCode.EMAIL_ALREADY_EXIST;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

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
}
