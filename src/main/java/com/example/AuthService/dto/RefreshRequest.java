package com.example.AuthService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RefreshRequest {

    @NotBlank(message = "Token is required")
    private String refreshToken;
}