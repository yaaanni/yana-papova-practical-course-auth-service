package com.example.AuthService.entity;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CredentialUserDetails implements UserDetails {

    private Credential credential;

    public CredentialUserDetails(Credential credential) {
        this.credential = credential;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(credential.getRole().name()));
    }

    @Override
    public @Nullable String getPassword() {
        return credential.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return credential.getUsername();
    }
}
