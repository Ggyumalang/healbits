package com.zerobase.healbits.transaction.controller;

import com.zerobase.healbits.transaction.dto.ChargeBalance;
import com.zerobase.healbits.transaction.dto.UseBalance;
import com.zerobase.healbits.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/use")
    public UseBalance.Response useBalance(
            @RequestBody @Valid UseBalance.Request request,
            @AuthenticationPrincipal User user
    ) {
        try {
            return UseBalance.Response.from(transactionService.useBalance(
                    request, user.getUsername()
            ));
        } catch (Exception e) {
            log.error("Failed to Use Balance By {} ", e.getMessage());
            transactionService.saveFailedUseBalance(
                    request,
                    user.getUsername()
            );
            throw e;
        }
    }

    @PostMapping("/charge")
    public ChargeBalance.Response chargeBalance(
            @RequestBody @Valid ChargeBalance.Request request,
            @AuthenticationPrincipal User user
    ) {
        try {
            return ChargeBalance.Response.from(transactionService.chargeBalance(
                    request, user.getUsername()
            ));
        } catch (Exception e) {
            log.error("Failed to Charge Balance By {} ", e.getMessage());
            transactionService.saveFailedChargeBalance(
                    request,
                    user.getUsername()
            );
            throw e;
        }
    }
}
