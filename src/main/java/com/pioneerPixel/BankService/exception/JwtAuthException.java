package com.pioneerPixel.BankService.exception;

public class JwtAuthException extends RuntimeException {
    public JwtAuthException(String message) {
        super(message);
    }
}
