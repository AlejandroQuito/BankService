package com.pioneerPixel.BankService.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String identifier,
        @NotBlank String password
) {
}
