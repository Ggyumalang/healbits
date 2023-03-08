package com.zerobase.healbits.controller;

import com.zerobase.healbits.dto.LoginMember;
import com.zerobase.healbits.dto.MemberInfo;
import com.zerobase.healbits.dto.RegisterMember;
import com.zerobase.healbits.dto.TokenInfo;
import com.zerobase.healbits.service.MemberService;
import lombok.RequiredArgsConstructor;
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
            @RequestParam("email") String email
    ){
        return memberService.getMemberInfo(email);
    }
}
