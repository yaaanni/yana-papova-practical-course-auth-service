package com.example.AuthService.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {

        super(message);
    }
}
