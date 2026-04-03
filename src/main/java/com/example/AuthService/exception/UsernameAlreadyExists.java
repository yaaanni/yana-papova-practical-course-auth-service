package com.example.AuthService.exception;

public class  UsernameAlreadyExists extends RuntimeException {
    public UsernameAlreadyExists(String message) {

        super("User with username " + message + "  already exists");
    }
}
