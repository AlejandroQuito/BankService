package com.pioneerPixel.BankService.service;

import com.pioneerPixel.BankService.entity.User;
import com.pioneerPixel.BankService.repository.EmailDataRepository;
import com.pioneerPixel.BankService.repository.PhoneDataRepository;
import com.pioneerPixel.BankService.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        boolean isEmail = identifier.contains("@");

        User user = isEmail
                ? emailDataRepository.findUserByIdentifier(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + identifier))
                : phoneDataRepository.findUserByIdentifier(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Phone not found: " + identifier));

        return new CustomUserDetails(
                user.getId(),
                identifier,
                user.getPassword()
        );
    }

    public boolean isEmailAvailable(String email) {
        return !emailDataRepository.existsByEmailIgnoreCase(email);
    }

    public boolean isPhoneAvailable(String phone) {
        return !phoneDataRepository.existsByPhone(phone);
    }
}
