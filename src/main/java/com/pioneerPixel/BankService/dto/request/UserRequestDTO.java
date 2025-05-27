package com.pioneerPixel.BankService.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserRequestDTO(
        @NotBlank String name,
        @NotNull @Past LocalDate dateOfBirth,
        @NotBlank @Size(min = 8) String password,
        @NotNull @PositiveOrZero BigDecimal initialBalance,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "^\\d{11}$") String phone
) {
}
