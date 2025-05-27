package com.pioneerPixel.BankService.repository;

import com.pioneerPixel.BankService.entity.EmailData;
import com.pioneerPixel.BankService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {

    @Query("SELECT e.user FROM EmailData e WHERE e.email = :identifier")
    Optional<User> findUserByIdentifier(@Param("identifier") String identifier);

    boolean existsByEmailIgnoreCase(String email);

    long countByUserId(Long userId);

    void deleteByUser_IdAndEmail(Long userId, String email);
}
