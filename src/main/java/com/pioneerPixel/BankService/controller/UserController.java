package com.pioneerPixel.BankService.controller;

import com.pioneerPixel.BankService.dto.request.EmailRequestDTO;
import com.pioneerPixel.BankService.dto.request.PhoneRequestDTO;
import com.pioneerPixel.BankService.dto.responce.UserResponseDTO;
import com.pioneerPixel.BankService.security.CustomUserDetails;
import com.pioneerPixel.BankService.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return userService.getUserById(currentUser.getId());
    }

    @PostMapping("/emails")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEmail(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody EmailRequestDTO request) {
        userService.addEmail(currentUser.getId(), request);
    }

    @DeleteMapping("/emails/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmail(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable @Email String email) {
        userService.deleteEmail(currentUser.getId(), email);
    }

    @PostMapping("/phones")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPhone(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody PhoneRequestDTO request) {
        userService.addPhone(currentUser.getId(), request);
    }

    @DeleteMapping("/phones/{phone}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhone(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable @Pattern(regexp = "^\\d{11}$") String phone) {
        userService.deletePhone(currentUser.getId(), phone);
    }

    @GetMapping("/search")
    public Page<UserResponseDTO> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.searchUsers(name, email, phone, dateOfBirth, page, size);
    }
}
