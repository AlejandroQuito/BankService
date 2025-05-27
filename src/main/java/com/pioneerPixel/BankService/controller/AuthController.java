package com.pioneerPixel.BankService.controller;

import com.pioneerPixel.BankService.dto.request.AuthRequestDTO;
import com.pioneerPixel.BankService.dto.request.UpdateJwtRequestDTO;
import com.pioneerPixel.BankService.dto.request.UserRequestDTO;
import com.pioneerPixel.BankService.dto.responce.JwtResponseDTO;
import com.pioneerPixel.BankService.dto.responce.UserResponseDTO;
import com.pioneerPixel.BankService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponseDTO login(@Valid @RequestBody AuthRequestDTO request) {
        return authService.createAuthToken(request);
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createNewUser(@Valid @RequestBody UserRequestDTO request) {
        return authService.createNewUser(request);
    }

    @PostMapping("/refresh-tokens")
    public JwtResponseDTO attemptToRefreshToken(@Valid @RequestBody UpdateJwtRequestDTO request) {
        return authService.attemptToRefreshTokens(request);
    }
}
