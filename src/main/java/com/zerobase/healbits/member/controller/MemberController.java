package com.zerobase.healbits.member.controller;

import com.zerobase.healbits.member.dto.LoginMember;
import com.zerobase.healbits.member.dto.MemberInfo;
import com.zerobase.healbits.member.dto.RegisterMember;
import com.zerobase.healbits.common.dto.TokenInfo;
import com.zerobase.healbits.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/register")
    public RegisterMember.Response registerMember(
            @RequestBody @Valid RegisterMember.Request request
    ) {
        return RegisterMember.Response.from(
                memberService.registerMember(request)
        );
    }

    @PostMapping("/login")
    public TokenInfo login(
            @RequestBody @Valid LoginMember loginMember
    ) {
        return memberService.login(
                loginMember.getEmail(),
                loginMember.getPassword()
        );
    }

    @GetMapping("/info")
    public MemberInfo getMemberInfo(
            @AuthenticationPrincipal User user
    ){
        return memberService.getMemberInfo(user.getUsername());
    }
}
