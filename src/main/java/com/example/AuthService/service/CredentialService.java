package com.example.AuthService.service;

import com.example.AuthService.dto.*;
import com.example.AuthService.entity.Credential;
import com.example.AuthService.exception.InvalidTokenException;
import com.example.AuthService.exception.UserNotAuthenticated;
import com.example.AuthService.exception.UsernameAlreadyExists;
import com.example.AuthService.mapper.CredentialMapper;
import com.example.AuthService.repository.CredentialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private final BCryptPasswordEncoder encoder;
    private final CredentialMapper credentialMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegistrationRequest request) throws UsernameAlreadyExists {
        if (credentialRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExists(request.getUsername());
        }
        Credential credential = credentialMapper.toRegEntity(request);
        credential.setPasswordHash(encoder.encode(request.getPassword()));
        credentialRepository.save(credential);
    }

    public LoginResponse createTokens(LoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            Credential credential = credentialRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));
            String accessToken = jwtService.generateToken(credential.getUsername(), credential.getUserId(), credential.getRole());
            String refreshToken = jwtService.generateRefreshToken(credential.getUsername());
            return new LoginResponse(accessToken, refreshToken);
        }
        throw new UserNotAuthenticated("User not authenticated");
    }

    public RefreshResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtService.validateToken(refreshToken)
                || !"refresh".equals(jwtService.extractTokenType(refreshToken))) {
            throw new InvalidTokenException("Invalid token");
        }
        String username = jwtService.extractUsername(refreshToken);
        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        String newAccess = jwtService.generateToken(
                credential.getUsername(),
                credential.getUserId(),
                credential.getRole()
        );
        return new RefreshResponse(newAccess);
    }

    public Boolean validate(ValidateRequest request) {
        String token = request.getToken();
        return jwtService.validateToken(token);
    }
}
