package com.pioneerPixel.BankService.mapper;

import com.pioneerPixel.BankService.dto.AccountDTO;
import com.pioneerPixel.BankService.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDto(Account account);

    @Mapping(target = "user", ignore = true)
    Account toEntity(AccountDTO accountDto);
}
