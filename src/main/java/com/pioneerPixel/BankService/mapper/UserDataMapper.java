package com.pioneerPixel.BankService.mapper;

import com.pioneerPixel.BankService.dto.responce.UserResponseDTO;
import com.pioneerPixel.BankService.entity.EmailData;
import com.pioneerPixel.BankService.entity.PhoneData;
import com.pioneerPixel.BankService.entity.User;
import com.pioneerPixel.BankService.entity.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserDataMapper {

    @Mapping(target = "emails", expression = "java(mapEmails(user.getEmails()))")
    @Mapping(target = "phones", expression = "java(mapPhones(user.getPhones()))")
    UserData toUserData(User user);

    UserResponseDTO toUserResponseDTO(UserData userData);

    default List<String> mapEmails(List<EmailData> emailData) {
        if (emailData == null) return Collections.emptyList();
        return emailData.stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toList());
    }

    default List<String> mapPhones(List<PhoneData> phoneData) {
        if (phoneData == null) return Collections.emptyList();
        return phoneData.stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toList());
    }
}
