package com.pioneerPixel.BankService.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateJwtRequestDTO(
        @NotBlank(message = "Refresh token must not be blank")
        String refreshToken) {
}
