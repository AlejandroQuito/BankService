package com.pioneerPixel.BankService.service;

import com.pioneerPixel.BankService.dto.request.AuthRequestDTO;
import com.pioneerPixel.BankService.dto.request.UpdateJwtRequestDTO;
import com.pioneerPixel.BankService.dto.request.UserRequestDTO;
import com.pioneerPixel.BankService.dto.responce.JwtResponseDTO;
import com.pioneerPixel.BankService.dto.responce.UserResponseDTO;
import com.pioneerPixel.BankService.exception.EmailAlreadyExistsException;
import com.pioneerPixel.BankService.exception.JwtAuthException;
import com.pioneerPixel.BankService.exception.PhoneAlreadyExistsException;
import com.pioneerPixel.BankService.security.CustomUserDetails;
import com.pioneerPixel.BankService.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshJwtService refreshJwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    public UserResponseDTO createNewUser(UserRequestDTO request) {
        if (!userDetailsService.isEmailAvailable(request.email())) {
            throw new EmailAlreadyExistsException("Email " + request.email() + " is already taken.");
        }
        if (!userDetailsService.isPhoneAvailable(request.phone())) {
            throw new PhoneAlreadyExistsException("Phone " + request.phone() + " is already taken.");
        }

        return userService.createNewUser(request);
    }

    public JwtResponseDTO createAuthToken(AuthRequestDTO request) {
        verify(request);
        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService.loadUserByUsername(request.identifier());

        return generateAndSaveTokens(userDetails);
    }

    public JwtResponseDTO attemptToRefreshTokens(UpdateJwtRequestDTO request) {
        String oldRefreshToken = request.refreshToken();

        if (!refreshJwtService.existsByToken(oldRefreshToken)) {
            throw new JwtAuthException("The refresh token passed is not valid.");
        }

        CustomUserDetails userDetails =
                (CustomUserDetails) customUserDetailsService.loadUserByUsername(
                        jwtUtils.getUsername(oldRefreshToken));
        refreshJwtService.deleteByToken(oldRefreshToken);

        return generateAndSaveTokens(userDetails);
    }

    private JwtResponseDTO generateAndSaveTokens(CustomUserDetails userDetails) {
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);
        refreshJwtService.save(refreshToken, userDetails.getId());
        return new JwtResponseDTO(accessToken, refreshToken);
    }

    private void verify(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.identifier(),
                        request.password()
                )
        );
    }
}
