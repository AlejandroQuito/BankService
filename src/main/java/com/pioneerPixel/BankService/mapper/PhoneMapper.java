package com.pioneerPixel.BankService.mapper;

import com.pioneerPixel.BankService.dto.request.PhoneRequestDTO;
import com.pioneerPixel.BankService.dto.responce.PhoneResponseDTO;
import com.pioneerPixel.BankService.entity.PhoneData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    @Mapping(target = "id", source = "id")
    PhoneResponseDTO toDto(PhoneData phoneData);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    PhoneData toEntity(PhoneRequestDTO phoneRequestDTO);

    default List<String> mapPhones(List<PhoneData> phones) {
        return phones.stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toList());
    }
}
