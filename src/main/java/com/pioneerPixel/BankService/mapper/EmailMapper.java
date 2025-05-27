package com.pioneerPixel.BankService.mapper;

import com.pioneerPixel.BankService.dto.request.EmailRequestDTO;
import com.pioneerPixel.BankService.dto.responce.EmailResponseDTO;
import com.pioneerPixel.BankService.entity.EmailData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    EmailResponseDTO toDto(EmailData emailData);

    @Mapping(target = "user", ignore = true)
    EmailData toEntity(EmailRequestDTO emailRequestDTO);

    default List<String> mapEmails(List<EmailData> emails) {
        return emails.stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toList());
    }
}
