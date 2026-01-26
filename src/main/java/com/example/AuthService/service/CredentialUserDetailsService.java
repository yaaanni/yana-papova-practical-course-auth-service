package com.example.AuthService.service;

import com.example.AuthService.entity.Credential;
import com.example.AuthService.entity.CredentialUserDetails;
import com.example.AuthService.exception.UserNotFoundException;
import com.example.AuthService.repository.CredentialRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CredentialUserDetailsService implements UserDetailsService {

    private final CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return new CredentialUserDetails(credential);
    }
}
