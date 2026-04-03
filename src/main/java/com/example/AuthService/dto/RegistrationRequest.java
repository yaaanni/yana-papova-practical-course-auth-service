package com.example.AuthService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Role id required")
    private String role;

    @NotNull(message = "User ID is required")
    private Long userId;
}
