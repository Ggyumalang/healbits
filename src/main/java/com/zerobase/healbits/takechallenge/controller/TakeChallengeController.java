package com.zerobase.healbits.takechallenge.controller;

import com.zerobase.healbits.challenge.type.ChallengeStatus;
import com.zerobase.healbits.takechallenge.dto.CompleteTookChallenge;
import com.zerobase.healbits.takechallenge.dto.ParticipateChallenge;
import com.zerobase.healbits.takechallenge.dto.TakeChallengeInfo;
import com.zerobase.healbits.takechallenge.service.TakeChallengeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PatchMapping("/complete")
    public CompleteTookChallenge.Response completeTookChallenge(
        @RequestParam long takeChallengeId,
        @AuthenticationPrincipal User user
    ) {
        return takeChallengeService
            .completeTookChallenge(takeChallengeId, user.getUsername());
    }
}
