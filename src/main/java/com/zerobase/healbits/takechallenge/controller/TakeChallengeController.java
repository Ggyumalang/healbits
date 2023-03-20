package com.zerobase.healbits.takechallenge.controller;

import com.zerobase.healbits.takechallenge.dto.ParticipateChallenge;
import com.zerobase.healbits.takechallenge.dto.TakeChallengeInfo;
import com.zerobase.healbits.takechallenge.service.TakeChallengeService;
import com.zerobase.healbits.type.ChallengeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/take-challenge")
public class TakeChallengeController {

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

    @GetMapping("/info")
    public List<TakeChallengeInfo> getTookChallengeInfoByChallengeStatus(
            @RequestParam ChallengeStatus challengeStatus,
            @AuthenticationPrincipal User user
    ) {
        return takeChallengeService.getTookChallengeListByChallengeStatus(
                challengeStatus, user.getUsername()
        );
    }
}
