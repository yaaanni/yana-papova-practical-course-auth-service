package com.example.AuthService.mapper;

import com.example.AuthService.dto.RegistrationRequest;
import com.example.AuthService.entity.Credential;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialMapper {

    Credential toRegEntity(RegistrationRequest request);
}
