package com.example.AuthService.controller;

import com.example.AuthService.dto.*;
import com.example.AuthService.service.CredentialService;
import com.example.AuthService.service.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class CredentialController {

    private final CredentialService credentialService;


    @PostMapping("/register")
    public ResponseEntity<Void> saveUserCredentials(@Valid @RequestBody RegistrationRequest request) {
        credentialService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> createToken(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = credentialService.createTokens(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestHeader("Authorization") String header) {
        RefreshResponse response = credentialService.refresh(header);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@Valid @RequestBody ValidateRequest request){
        Boolean response = credentialService.validate(request);
        return ResponseEntity.ok(response);
    }

}
