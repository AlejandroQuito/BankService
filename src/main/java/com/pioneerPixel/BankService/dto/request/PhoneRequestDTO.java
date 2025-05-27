package com.pioneerPixel.BankService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PhoneRequestDTO(
        @NotBlank @Pattern(regexp = "^\\d{11}$") String phone
) {
}
