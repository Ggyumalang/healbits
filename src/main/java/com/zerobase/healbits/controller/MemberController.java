package com.zerobase.healbits.controller;

import com.zerobase.healbits.dto.LoginMember;
import com.zerobase.healbits.dto.RegisterMember;
import com.zerobase.healbits.dto.TokenInfo;
import com.zerobase.healbits.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/member/register")
    public RegisterMember.Response registerMember(
            @RequestBody @Valid RegisterMember.Request request
    ) {
        return RegisterMember.Response.from(
                memberService.registerMember(request)
        );
    }

    @PostMapping("/member/login")
    public TokenInfo login(
            @RequestBody @Valid LoginMember loginMember
    ) {
        return memberService.login(
                loginMember.getEmail(),
                loginMember.getPassword()
        );
    }

    @GetMapping("/member/info")
    public String getMemberInfo(
            @RequestParam("email") String email
    ) {
        return "test";
    }
}
