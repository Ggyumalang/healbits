package com.zerobase.healbits.controller;

import com.zerobase.healbits.dto.RegisterChallenge;
import com.zerobase.healbits.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @PostMapping("/challenge/register")
    public RegisterChallenge.Response registerChallenge(
            @RequestBody @Valid RegisterChallenge.Request request
    ) {
        return RegisterChallenge.Response.from(
                challengeService.registerChallenge(request)
        );
    }
}
