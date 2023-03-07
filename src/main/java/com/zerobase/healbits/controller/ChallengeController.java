package com.zerobase.healbits.controller;

import com.zerobase.healbits.dto.ChallengeDetailInfo;
import com.zerobase.healbits.dto.ChallengeSummaryInfo;
import com.zerobase.healbits.dto.RegisterChallenge;
import com.zerobase.healbits.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/challenge/list")
    public List<ChallengeSummaryInfo> getChallengeListByCategory(
            @RequestParam String challengeCategory
    ) {
        return challengeService.getChallengeListByCategory(challengeCategory);
    }

    @GetMapping("/challenge/detail")
    public ChallengeDetailInfo getChallengeDetail(
            @RequestParam long challengeId
    ) {
        return ChallengeDetailInfo.from(challengeService.getChallengeDetail(challengeId));
    }
}
