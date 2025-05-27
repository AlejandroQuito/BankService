package com.pioneerPixel.BankService.service;

import com.pioneerPixel.BankService.config.InterestConfig;
import com.pioneerPixel.BankService.entity.Account;
import com.pioneerPixel.BankService.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final InterestConfig interestConfig;

    public BigDecimal getBalance(Long userId) {
        return getAccount(userId).getBalance();
    }

    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        validateAmountPositive(amount);
        Account account = getAccount(userId);

        account.setBalance(account.getBalance().add(amount));
    }

    @Transactional
    public void withdraw(Long userId, BigDecimal amount) {
        validateAmountPositive(amount);
        Account account = getAccount(userId);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(amount));
    }

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        validateAmountPositive(amount);

        Account fromAccount = getAccount(fromUserId);
        Account toAccount = getAccount(toUserId);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
    }

    @Transactional
    public void increaseBalances() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            BigDecimal maxBalance = account
                    .getInitialBalance().multiply(interestConfig.getMaxRatio());
            BigDecimal increasedBalance = account
                    .getBalance().multiply(interestConfig.getMultiplier());

            if (increasedBalance.compareTo(maxBalance) > 0) {
                account.setBalance(maxBalance);
            } else {
                account.setBalance(increasedBalance);
            }
            accountRepository.save(account);
        }
    }

    private void validateAmountPositive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private Account getAccount(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for userId: " + userId));
    }
}
