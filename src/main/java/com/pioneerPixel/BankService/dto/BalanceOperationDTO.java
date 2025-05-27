package com.pioneerPixel.BankService.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BalanceOperationDTO(
        @NotNull @Positive BigDecimal amount
) {
}
