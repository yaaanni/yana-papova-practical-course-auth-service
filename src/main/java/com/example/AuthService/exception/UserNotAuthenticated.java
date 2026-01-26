package com.example.AuthService.exception;

public class UserNotAuthenticated extends RuntimeException {
    public UserNotAuthenticated(String message) {

        super(message);
    }
}
