package com.zerobase.healbits.controller;

import com.zerobase.healbits.dto.RegisterMember;
import com.zerobase.healbits.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
