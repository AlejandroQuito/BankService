package com.pioneerPixel.BankService.service;

import com.pioneerPixel.BankService.entity.EmailData;
import com.pioneerPixel.BankService.entity.PhoneData;
import com.pioneerPixel.BankService.entity.User;
import com.pioneerPixel.BankService.entity.UserData;
import com.pioneerPixel.BankService.repository.UserRepository;
import com.pioneerPixel.BankService.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSyncService {

    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;

    @Transactional
    @Scheduled(fixedRate = 300000)
    public void syncUsersToElasticsearch() {
        List<UserData> indices = userRepository.findAll().stream()
                .map(this::mapToIndex)
                .collect(Collectors.toList());

        userSearchRepository.saveAll(indices);
    }

    private UserData mapToIndex(User user) {
        UserData index = new UserData();
        index.setId(user.getId());
        index.setName(user.getName());
        index.setDateOfBirth(user.getDateOfBirth());

        index.setEmails(Optional.ofNullable(user.getEmails())
                .orElse(List.of())
                .stream()
                .map(EmailData::getEmail)
                .toList());

        index.setPhones(Optional.ofNullable(user.getPhones())
                .orElse(List.of())
                .stream()
                .map(PhoneData::getPhone)
                .toList());

        return index;
    }
}
