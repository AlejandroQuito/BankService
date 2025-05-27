package com.pioneerPixel.BankService.repository;

import com.pioneerPixel.BankService.entity.UserData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomUserSearchRepository {
    Page<UserData> searchUsers(String name,
                               String email,
                               String phone,
                               LocalDate dateOfBirth,
                               Pageable pageable);
}
