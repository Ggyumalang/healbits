package com.zerobase.healbits.verification.controller;

import com.zerobase.healbits.verification.dto.VerificationInfo;
import com.zerobase.healbits.verification.dto.VerifyChallenge;
import com.zerobase.healbits.verification.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/verification")
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping
    public VerifyChallenge.Response verifyChallenge(
            @RequestPart long verifiedTakeChallengeId,
            @RequestPart MultipartFile multipartFile,
            @AuthenticationPrincipal User user
    ) {
        return VerifyChallenge.Response.from(verificationService.verifyChallenge(
                verifiedTakeChallengeId
                , multipartFile
                , user.getUsername()
        ));
    }

    @GetMapping
    public VerificationInfo getVerificationInfo(
            @RequestParam long verifiedTakeChallengeId
    ){
        return verificationService.getVerificationInfo(verifiedTakeChallengeId);
    }

}
