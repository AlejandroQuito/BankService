package com.pioneerPixel.BankService.dto.responce;

import com.pioneerPixel.BankService.dto.AccountDTO;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record UserResponseDTO(
        Long id,
        String name,
        LocalDate dateOfBirth,
        AccountDTO account,
        List<String> emails,
        List<String> phones
) {
}
