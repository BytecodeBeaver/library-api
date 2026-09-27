package com.github.bytecodebeaver.libraryapi.security.controller;

import com.github.bytecodebeaver.libraryapi.security.dto.AuthCredentialsDTO;
import com.github.bytecodebeaver.libraryapi.security.dto.AuthTokenResponseDTO;
import com.github.bytecodebeaver.libraryapi.security.dto.MemberRegistrationRequestDTO;
import com.github.bytecodebeaver.libraryapi.security.service.JwtAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth/jwt")
@RequiredArgsConstructor
public class JwtAuthController {
    private final JwtAuthService jwtAuthService;

    @PostMapping("/auth-token")
    public AuthTokenResponseDTO authenticate(@Valid @RequestBody AuthCredentialsDTO credentials) {
        String token = jwtAuthService.getAuthorizationToken(credentials.username(), credentials.password());
        return new AuthTokenResponseDTO(token, "Bearer", jwtAuthService.getAccessTokenTtlSeconds());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody MemberRegistrationRequestDTO registrationData) {
        jwtAuthService.register(
                registrationData.firstName(),
                registrationData.lastName(),
                registrationData.email(),
                registrationData.password(),
                registrationData.phoneNumber()
        );
    }
}
