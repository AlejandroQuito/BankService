package com.pioneerPixel.BankService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record AuthRequestDTO(
        @NotBlank
        @Pattern(regexp = ".+@.+\\..+|\\d{11}", message = "Должен быть email или 11-значный телефон")
        String identifier,

        @NotBlank String password
) {
}
