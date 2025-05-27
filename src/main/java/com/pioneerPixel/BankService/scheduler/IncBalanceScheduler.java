package com.pioneerPixel.BankService.scheduler;

import com.pioneerPixel.BankService.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncBalanceScheduler {

    private final AccountService accountService;

    @Async(value = "balance")
    @Scheduled(cron = "${scheduler.increase-balance-cron}")
    public void increaseBalance() {
        accountService.increaseBalances();
    }
}
