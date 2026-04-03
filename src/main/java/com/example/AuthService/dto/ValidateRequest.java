package com.example.AuthService.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateRequest {

    @NotBlank(message = "Token is required")
    private String token;
}
