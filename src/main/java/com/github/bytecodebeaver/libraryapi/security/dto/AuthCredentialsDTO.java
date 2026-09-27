package com.github.bytecodebeaver.libraryapi.security.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthCredentialsDTO(
        @NotBlank String username,
        @NotBlank String password
) {
}
