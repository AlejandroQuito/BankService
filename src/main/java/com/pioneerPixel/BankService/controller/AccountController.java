package com.pioneerPixel.BankService.controller;

import com.pioneerPixel.BankService.security.CustomUserDetails;
import com.pioneerPixel.BankService.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/balance")
    public BigDecimal getBalance(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return accountService.getBalance(userDetails.getId());
    }

    @PostMapping("/deposit")
    public void deposit(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam BigDecimal amount) {
        accountService.deposit(userDetails.getId(), amount);
    }

    @PostMapping("/withdraw")
    public void withdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam BigDecimal amount) {
        accountService.withdraw(userDetails.getId(), amount);
    }

    @PostMapping("/transfer")
    public void transfer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long recipientUserId,
            @RequestParam BigDecimal amount) {
        accountService.transfer(userDetails.getId(), recipientUserId, amount);
    }
}
