package com.github.bytecodebeaver.libraryapi.security.controller;

import com.github.bytecodebeaver.libraryapi.security.dto.AuthCredentialsDTO;
import com.github.bytecodebeaver.libraryapi.security.dto.AuthTokenResponseDTO;
import com.github.bytecodebeaver.libraryapi.security.dto.MemberRegistrationRequestDTO;
import com.github.bytecodebeaver.libraryapi.security.service.JwtAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthControllerTest {
    @Mock
    private JwtAuthService jwtAuthService;

    private JwtAuthController controller;

    @BeforeEach
    void setUp() {
        controller = new JwtAuthController(jwtAuthService);
    }

    @Test
    void authenticate_returnsBearerTokenAndTtl() {
        AuthCredentialsDTO credentials = new AuthCredentialsDTO("jane@example.com", "password123");
        when(jwtAuthService.getAuthorizationToken("jane@example.com", "password123")).thenReturn("signed-token");
        when(jwtAuthService.getAccessTokenTtlSeconds()).thenReturn(900L);

        assertThat(controller.authenticate(credentials))
                .isEqualTo(new AuthTokenResponseDTO("signed-token", "Bearer", 900));
    }

    @Test
    void register_passesAllRequestFieldsToAuthService() {
        MemberRegistrationRequestDTO request =
                new MemberRegistrationRequestDTO("Jane", "Doe", "jane@example.com", "password123", "555-0100");

        controller.register(request);

        verify(jwtAuthService).register(
                "Jane", "Doe", "jane@example.com", "password123", "555-0100");
    }

    @Test
    void register_declaresCreatedStatus() throws NoSuchMethodException {
        ResponseStatus status = JwtAuthController.class
                .getMethod("register", MemberRegistrationRequestDTO.class)
                .getAnnotation(ResponseStatus.class);

        assertThat(status.value()).isEqualTo(HttpStatus.CREATED);
    }
}
