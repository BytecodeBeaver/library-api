package com.github.bytecodebeaver.libraryapi.security.dto;

public record AuthTokenResponseDTO(String accessToken, String tokenType, long expiresInSeconds) {
}
