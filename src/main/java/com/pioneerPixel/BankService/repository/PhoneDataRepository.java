package com.pioneerPixel.BankService.repository;

import com.pioneerPixel.BankService.entity.PhoneData;
import com.pioneerPixel.BankService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {

    @Query("SELECT p.user FROM PhoneData p WHERE p.phone = :identifier")
    Optional<User> findUserByIdentifier(@Param("identifier") String identifier);

    void deleteByUserIdAndPhone(Long userId, String phone);

    long countByUserId(Long userId);

    boolean existsByPhone(String phone);
}
