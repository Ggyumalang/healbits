package com.zerobase.healbits.challenge.controller;

import com.zerobase.healbits.challenge.dto.ChallengeDetailInfo;
import com.zerobase.healbits.challenge.dto.ChallengeSummaryInfo;
import com.zerobase.healbits.challenge.dto.RegisterChallenge;
import com.zerobase.healbits.challenge.service.ChallengeService;
import com.zerobase.healbits.common.type.ChallengeCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    @PostMapping("/register")
    public RegisterChallenge.Response registerChallenge(
            @RequestBody @Valid RegisterChallenge.Request request,
            @AuthenticationPrincipal User user
    ) {
        return RegisterChallenge.Response.from(
                challengeService.registerChallenge(request, user.getUsername())
        );
    }

    @GetMapping("/list")
    public Slice<ChallengeSummaryInfo> getChallengeListByCategory(
            @RequestParam ChallengeCategory challengeCategory,
            @PageableDefault Pageable pageable
    ) {
        return challengeService.getChallengeListByCategory(challengeCategory, pageable);
    }

    @GetMapping("/detail")
    public ChallengeDetailInfo getChallengeDetail(
            @RequestParam long challengeId
    ) {
        return ChallengeDetailInfo.from(challengeService.getChallengeDetail(challengeId));
    }
}
