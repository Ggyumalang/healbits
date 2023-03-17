package com.zerobase.healbits.takechallenge.controller;

import com.zerobase.healbits.takechallenge.dto.ParticipateChallenge;
import com.zerobase.healbits.takechallenge.service.TakeChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/take-challenge")
public class TakeChallengeController {

    private static final String authorization = "Authorization";

    private final TakeChallengeService takeChallengeService;

    @PostMapping("/participate")
    public ParticipateChallenge.Response participateChallenge(
            @RequestBody ParticipateChallenge.Request request,
            @AuthenticationPrincipal User user
    ) {
        return ParticipateChallenge.Response.from(
                takeChallengeService.participateChallenge(
                        request, user.getUsername()
                )
        );
    }
}
