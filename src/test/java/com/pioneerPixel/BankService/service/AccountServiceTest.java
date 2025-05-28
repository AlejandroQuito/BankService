package com.pioneerPixel.BankService.service;

import com.pioneerPixel.BankService.config.InterestConfig;
import com.pioneerPixel.BankService.entity.Account;
import com.pioneerPixel.BankService.exception.AccountNotFoundException;
import com.pioneerPixel.BankService.exception.InsufficientFundsException;
import com.pioneerPixel.BankService.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private InterestConfig interestConfig;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setInitialBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void getBalance_shouldReturnCorrectBalance() {
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(account));

        BigDecimal result = accountService.getBalance(1L);

        assertEquals(BigDecimal.valueOf(1000), result);
    }

    @Test
    void getBalance_shouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getBalance(1L));
    }

    @Test
    void deposit_shouldAddAmountToBalance() {
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(account));

        accountService.deposit(1L, BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(1500), account.getBalance());
    }

    @Test
    void deposit_shouldThrowExceptionWhenAccountNotFound() {
        Long userId = 999L;
        BigDecimal amount = BigDecimal.valueOf(500);

        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.deposit(userId, amount));
    }

    @Test
    void withdraw_shouldSubtractAmountFromBalance() {
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(account));

        accountService.withdraw(1L, BigDecimal.valueOf(300));

        assertEquals(BigDecimal.valueOf(700), account.getBalance());
    }

    @Test
    void withdraw_shouldThrowIfInsufficientFunds() {
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () ->
                accountService.withdraw(1L, BigDecimal.valueOf(2000))
        );
    }

    @Test
    void withdraw_shouldThrowExceptionWhenAccountNotFound() {
        Long userId = 999L;
        BigDecimal amount = BigDecimal.valueOf(300);

        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.withdraw(userId, amount));
    }

    @Test
    void withdraw_shouldThrowExceptionWhenInsufficientFunds() {
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(account));

        BigDecimal amount = BigDecimal.valueOf(2000);

        assertThrows(InsufficientFundsException.class,
                () -> accountService.withdraw(1L, amount));
    }

    @Test
    void withdraw_shouldThrowExceptionWhenAmountIsNegativeOrZero() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.withdraw(1L, BigDecimal.ZERO));
    }

    @Test
    void transfer_shouldTransferAmountBetweenAccounts() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(500));

        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findByUserId(2L)).thenReturn(Optional.of(toAccount));

        accountService.transfer(1L, 2L, BigDecimal.valueOf(400));

        assertEquals(BigDecimal.valueOf(600), account.getBalance());
        assertEquals(BigDecimal.valueOf(900), toAccount.getBalance());
    }

    @Test
    void transfer_shouldThrowIfInsufficientFunds() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(500));

        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findByUserId(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(InsufficientFundsException.class, () ->
                accountService.transfer(1L, 2L, BigDecimal.valueOf(5000))
        );
    }

    @Test
    void increaseBalances_shouldNotExceedMaxRatio() {
        account.setBalance(BigDecimal.valueOf(2000));
        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(interestConfig.getMultiplier()).thenReturn(BigDecimal.valueOf(1.5));
        when(interestConfig.getMaxRatio()).thenReturn(BigDecimal.valueOf(2.0));

        accountService.increaseBalances();

        BigDecimal expectedMax = account.getInitialBalance().multiply(BigDecimal.valueOf(2.0));
        assertEquals(expectedMax, account.getBalance());
    }

    @Test
    void increaseBalances_shouldMultiplyWithinMax() {
        account.setBalance(BigDecimal.valueOf(1000));
        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(interestConfig.getMultiplier()).thenReturn(BigDecimal.valueOf(1.1));
        when(interestConfig.getMaxRatio()).thenReturn(BigDecimal.valueOf(2.0));

        accountService.increaseBalances();

        BigDecimal expected = BigDecimal.valueOf(1100.0);
        assertEquals(expected, account.getBalance());
    }

    @Test
    void transfer_shouldThrowException_whenInsufficientFunds() {
        Account sender = new Account();
        sender.setId(1L);
        sender.setBalance(BigDecimal.valueOf(500));

        Account receiver = new Account();
        receiver.setId(2L);
        receiver.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUserId(2L)).thenReturn(Optional.of(receiver));

        BigDecimal transferAmount = BigDecimal.valueOf(600);

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.transfer(1L, 2L, transferAmount);
        });

        assertEquals(BigDecimal.valueOf(500), sender.getBalance());
        assertEquals(BigDecimal.valueOf(1000), receiver.getBalance());
    }

    @Test
    void transfer_shouldThrowException_whenAmountIsNegativeOrZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer(1L, 2L, BigDecimal.ZERO);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer(1L, 2L, BigDecimal.valueOf(-10));
        });
    }

    @Test
    void transfer_shouldThrowException_whenSenderAccountNotFound() {
        when(accountRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.transfer(1L, 2L, BigDecimal.valueOf(100));
        });
    }

    @Test
    void transfer_shouldThrowException_whenReceiverAccountNotFound() {
        Account sender = new Account();
        sender.setId(1L);
        sender.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findByUserId(1L)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUserId(2L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.transfer(1L, 2L, BigDecimal.valueOf(100));
        });
    }

    @Test
    void transfer_shouldThrowException_whenSenderAndReceiverAreSame() {
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer(1L, 1L, BigDecimal.valueOf(100));
        });
    }
}
