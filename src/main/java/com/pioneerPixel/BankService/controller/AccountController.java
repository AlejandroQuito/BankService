package com.pioneerPixel.BankService.controller;

import com.pioneerPixel.BankService.security.CustomUserDetails;
import com.pioneerPixel.BankService.service.AccountService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1/accounts")
@Validated
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
            @RequestParam @Min(value = 0, message = "Amount must be positive") BigDecimal amount) {
        accountService.deposit(userDetails.getId(), amount);
    }

    @PostMapping("/withdraw")
    public void withdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam @Min(value = 0, message = "Amount must be positive") BigDecimal amount) {
        accountService.withdraw(userDetails.getId(), amount);
    }

    @PostMapping("/transfer")
    public void transfer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long recipientUserId,
            @RequestParam @Min(value = 0, message = "Amount must be positive") BigDecimal amount) {
        accountService.transfer(userDetails.getId(), recipientUserId, amount);
    }
}
