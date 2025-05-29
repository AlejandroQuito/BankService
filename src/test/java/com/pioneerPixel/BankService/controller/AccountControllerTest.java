package com.pioneerPixel.BankService.controller;

import com.pioneerPixel.BankService.config.SecurityConfig;
import com.pioneerPixel.BankService.exception.GlobalExceptionHandler;
import com.pioneerPixel.BankService.repository.AccountRepository;
import com.pioneerPixel.BankService.repository.EmailDataRepository;
import com.pioneerPixel.BankService.repository.PhoneDataRepository;
import com.pioneerPixel.BankService.security.CustomUserDetails;
import com.pioneerPixel.BankService.security.JwtRequestFilter;
import com.pioneerPixel.BankService.security.SecurityConstants;
import com.pioneerPixel.BankService.service.AccountService;
import com.pioneerPixel.BankService.service.CustomUserDetailsService;
import com.pioneerPixel.BankService.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@ContextConfiguration(classes = {
        SecurityConfig.class,
        AccountController.class,
        CustomUserDetailsService.class,
        AccountService.class,
        EmailDataRepository.class,
        JwtRequestFilter.class,
        JwtUtils.class,
        GlobalExceptionHandler.class})
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private EmailDataRepository emailDataRepository;

    @MockitoBean
    private PhoneDataRepository phoneDataRepository;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private SecurityConstants securityConstants;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private CustomUserDetails customUserDetails;

    private CustomUserDetails userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = new CustomUserDetails(1L, "user", "pass");
    }

    @Test
    void getBalance_shouldReturnCorrectBalance() throws Exception {
        when(accountService.getBalance(1L)).thenReturn(BigDecimal.valueOf(1000));

        mockMvc.perform(get("/v1/accounts/balance")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().string("1000"));
    }

    @Test
    void getBalance_shouldReturnUnauthorized_ifNoUser() throws Exception {
        mockMvc.perform(get("/v1/accounts/balance"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deposit_shouldCallService() throws Exception {
        mockMvc.perform(post("/v1/accounts/deposit")
                        .param("amount", "500")
                        .with(user(userDetails)))
                .andExpect(status().isOk());

        verify(accountService).deposit(1L, BigDecimal.valueOf(500));
    }

    @Test
    void deposit_shouldReturnBadRequest_ifAmountMissing() throws Exception {
        mockMvc.perform(post("/v1/accounts/deposit")
                        .with(user(userDetails)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deposit_shouldReturnBadRequest_ifAmountInvalid() throws Exception {
        mockMvc.perform(post("/v1/accounts/deposit")
                        .param("amount", "invalid")
                        .with(user(userDetails)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deposit_shouldReturnServerError_ifServiceThrows() throws Exception {
        doThrow(new RuntimeException("Service failed"))
                .when(accountService).deposit(anyLong(), eq(BigDecimal.valueOf(500)));

        mockMvc.perform(post("/v1/accounts/deposit")
                        .param("amount", "500")
                        .with(user(userDetails)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void withdraw_shouldCallService() throws Exception {
        mockMvc.perform(post("/v1/accounts/withdraw")
                        .param("amount", "200")
                        .with(user(userDetails)))
                .andExpect(status().isOk());

        verify(accountService).withdraw(1L, BigDecimal.valueOf(200));
    }

    @Test
    void withdraw_shouldReturnBadRequest_ifAmountMissing() throws Exception {
        mockMvc.perform(post("/v1/accounts/withdraw")
                        .with(user(userDetails)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).withdraw(anyLong(), any());
    }

    @Test
    void withdraw_shouldReturnBadRequest_ifAmountNegative() throws Exception {
        mockMvc.perform(post("/v1/accounts/withdraw")
                        .param("amount", "-100")
                        .with(user(userDetails)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).withdraw(anyLong(), any());
    }

    @Test
    void transfer_shouldCallService() throws Exception {
        mockMvc.perform(post("/v1/accounts/transfer")
                        .param("recipientUserId", "2")
                        .param("amount", "300")
                        .with(user(userDetails)))
                .andExpect(status().isOk());

        verify(accountService).transfer(1L, 2L, BigDecimal.valueOf(300));
    }

    @Test
    void transfer_shouldReturnBadRequest_ifRecipientMissing() throws Exception {
        mockMvc.perform(post("/v1/accounts/transfer")
                        .param("amount", "100")
                        .with(user(userDetails)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_shouldReturnBadRequest_ifAmountMissing() throws Exception {
        mockMvc.perform(post("/v1/accounts/transfer")
                        .param("recipientUserId", "2")
                        .with(user(userDetails)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_shouldReturnServerError_ifServiceThrows() throws Exception {
        doThrow(new RuntimeException("Transfer failed"))
                .when(accountService).transfer(1L, 2L, BigDecimal.valueOf(300));

        mockMvc.perform(post("/v1/accounts/transfer")
                        .param("recipientUserId", "2")
                        .param("amount", "300")
                        .with(user(userDetails)))
                .andExpect(status().isInternalServerError());
    }
}
