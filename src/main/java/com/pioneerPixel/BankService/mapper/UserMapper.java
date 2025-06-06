package com.pioneerPixel.BankService.mapper;

import com.pioneerPixel.BankService.dto.request.UserRequestDTO;
import com.pioneerPixel.BankService.dto.responce.UserResponseDTO;
import com.pioneerPixel.BankService.entity.EmailData;
import com.pioneerPixel.BankService.entity.PhoneData;
import com.pioneerPixel.BankService.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {AccountMapper.class, EmailMapper.class, PhoneMapper.class})
public interface UserMapper {

    @Mapping(target = "account", source = "account")
    @Mapping(target = "emails", expression = "java(mapEmailsToStringList(user.getEmails()))")
    @Mapping(target = "phones", expression = "java(mapPhonesToStringList(user.getPhones()))")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    UserResponseDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account.initialBalance", source = "initialBalance")
    @Mapping(target = "account.balance", source = "initialBalance")
    @Mapping(target = "account.user", source = ".")
    @Mapping(target = "emails", expression = "java(mapEmails(userDto.email()))")
    @Mapping(target = "phones", expression = "java(mapPhones(userDto.phone()))")
    User toEntity(UserRequestDTO userDto);

    default List<EmailData> mapEmails(String email) {
        if (email == null) return Collections.emptyList();

        EmailData emailData = new EmailData();
        emailData.setEmail(email);
        return List.of(emailData);
    }

    default List<PhoneData> mapPhones(String phone) {
        if (phone == null) return Collections.emptyList();

        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(phone);
        return List.of(phoneData);
    }

    default List<String> mapEmailsToStringList(List<EmailData> emails) {
        if (emails == null) return Collections.emptyList();

        return emails.stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toList());
    }

    default List<String> mapPhonesToStringList(List<PhoneData> phones) {
        if (phones == null) return Collections.emptyList();

        return phones.stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toList());
    }

    @AfterMapping
    default void linkUserToChildren(@MappingTarget User user) {
        if (user.getAccount() != null) {
            user.getAccount().setUser(user);
        }

        if (user.getEmails() != null) {
            user.getEmails().forEach(email -> email.setUser(user));
        }

        if (user.getPhones() != null) {
            user.getPhones().forEach(phone -> phone.setUser(user));
        }
    }
}
