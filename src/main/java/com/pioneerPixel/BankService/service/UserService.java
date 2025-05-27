package com.pioneerPixel.BankService.service;

import com.pioneerPixel.BankService.dto.request.EmailRequestDTO;
import com.pioneerPixel.BankService.dto.request.PhoneRequestDTO;
import com.pioneerPixel.BankService.dto.request.UserRequestDTO;
import com.pioneerPixel.BankService.dto.responce.UserResponseDTO;
import com.pioneerPixel.BankService.entity.Account;
import com.pioneerPixel.BankService.entity.EmailData;
import com.pioneerPixel.BankService.entity.PhoneData;
import com.pioneerPixel.BankService.entity.User;
import com.pioneerPixel.BankService.entity.UserData;
import com.pioneerPixel.BankService.exception.EmailAlreadyExistsException;
import com.pioneerPixel.BankService.exception.LastEmailDeletionException;
import com.pioneerPixel.BankService.exception.LastPhoneDeletionException;
import com.pioneerPixel.BankService.exception.PhoneAlreadyExistsException;
import com.pioneerPixel.BankService.exception.UserNotFoundException;
import com.pioneerPixel.BankService.mapper.UserDataMapper;
import com.pioneerPixel.BankService.mapper.UserMapper;
import com.pioneerPixel.BankService.repository.EmailDataRepository;
import com.pioneerPixel.BankService.repository.PhoneDataRepository;
import com.pioneerPixel.BankService.repository.UserRepository;
import com.pioneerPixel.BankService.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSearchRepository userSearchRepository;
    private final UserMapper userMapper;
    private final UserDataMapper userDataMapper;

    public Page<UserResponseDTO> searchUsers(String name,
                                             String email,
                                             String phone,
                                             LocalDate dateOfBirth,
                                             int page,
                                             int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserData> result = userSearchRepository.searchUsers(name, email, phone, dateOfBirth, pageable);
        return result.map(userDataMapper::toUserResponseDTO);
    }

    @CachePut(value = "users", key = "#result.id")
    @Transactional
    public UserResponseDTO createNewUser(UserRequestDTO request) {
        validateRegistrationRequest(request);

        User user = User.builder()
                .name(request.name())
                .dateOfBirth(request.dateOfBirth())
                .password(passwordEncoder.encode(request.password()))
                .build();

        EmailData email = EmailData.builder()
                .email(request.email())
                .user(user)
                .build();

        PhoneData phone = PhoneData.builder()
                .phone(request.phone())
                .user(user)
                .build();

        Account account = Account.builder()
                .balance(request.initialBalance())
                .initialBalance(request.initialBalance())
                .user(user)
                .build();

        user.setEmails(List.of(email));
        user.setPhones(List.of(phone));
        user.setAccount(account);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public void addEmail(Long userId, EmailRequestDTO request) {
        if (emailDataRepository.existsByEmailIgnoreCase(request.email())) {
            throw new EmailAlreadyExistsException("Email " + request.email() + " is already taken.");
        }

        User user = getUser(userId);
        EmailData email = new EmailData();
        email.setEmail(request.email());
        email.setUser(user);
        emailDataRepository.save(email);
    }

    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public void deleteEmail(Long userId, String email) {
        if (emailDataRepository.countByUserId(userId) <= 1) {
            throw new LastEmailDeletionException("User must have at least one email");
        }
        emailDataRepository.deleteByUser_IdAndEmail(userId, email);
    }

    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public void addPhone(Long userId, PhoneRequestDTO request) {
        if (phoneDataRepository.existsByPhone(request.phone())) {
            throw new PhoneAlreadyExistsException("Phone " + request.phone() + " is already taken.");
        }

        User user = getUser(userId);
        PhoneData phone = new PhoneData();
        phone.setPhone(request.phone());
        phone.setUser(user);
        phoneDataRepository.save(phone);
    }

    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public void deletePhone(Long userId, String phone) {
        if (phoneDataRepository.countByUserId(userId) <= 1) {
            throw new LastPhoneDeletionException("User must have at least one phone");
        }
        phoneDataRepository.deleteByUserIdAndPhone(userId, phone);
    }

    @Cacheable(value = "users", key = "#userId")
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long userId) {
        User user = getUser(userId);
        return userMapper.toDto(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found id: " + userId));
    }

    private void validateRegistrationRequest(UserRequestDTO request) {
        if (request.initialBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
    }
}
