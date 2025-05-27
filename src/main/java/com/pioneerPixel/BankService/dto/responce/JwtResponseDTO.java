package com.pioneerPixel.BankService.dto.responce;

public record JwtResponseDTO(
        String accessToken,
        String refreshToken) {
}
