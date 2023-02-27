package com.zerobase.healbits.service;

import com.zerobase.healbits.domain.Member;
import com.zerobase.healbits.dto.MemberDto;
import com.zerobase.healbits.dto.RegisterMember;
import com.zerobase.healbits.exception.HealBitsException;
import com.zerobase.healbits.repository.MemberRepository;
import com.zerobase.healbits.type.ErrorCode;
import com.zerobase.healbits.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberDto registerMember(RegisterMember.Request request) {

        checkExistedEmail(request.getEmail());

        return MemberDto.fromEntity(memberRepository.save(Member.builder()
                .email(request.getEmail())
                .password(PasswordUtil.encPassword(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .balance(request.getBalance())
                .build()));
    }

    private void checkExistedEmail(@NotBlank String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            throw new HealBitsException(ErrorCode.EMAIL_ALREADY_EXIST);
        }
    }
}
