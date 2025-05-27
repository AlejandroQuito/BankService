package com.pioneerPixel.BankService.dto;

import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AccountDTO(
        Long id,
        @PositiveOrZero BigDecimal balance,
        @PositiveOrZero BigDecimal initialBalance
) {
}
