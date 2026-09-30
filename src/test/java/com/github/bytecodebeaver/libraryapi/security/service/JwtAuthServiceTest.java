package com.github.bytecodebeaver.libraryapi.security.service;

import com.github.bytecodebeaver.libraryapi.model.entity.Member;
import com.github.bytecodebeaver.libraryapi.security.exceptions.EmailAlreadyInUseException;
import com.github.bytecodebeaver.libraryapi.service.MemberRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthServiceTest {

    private static final String VALID_EMAIL = "admin@localhost";
    private static final String VALID_PASSWORD = "admin";
    private static final String TOKEN = "jwt-token";

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private MemberRegistrationService memberRegistrationService;

    private JwtAuthService jwtAuthService;
    private Member admin;

    @BeforeEach
    void setUp() {
        jwtAuthService = new JwtAuthService(jwtService, authenticationService, memberRegistrationService);

        admin = new Member();
        admin.setEmail(VALID_EMAIL);
        admin.setRoles(List.of("ROLE_ADMIN"));
        admin.setActive(true);
    }

    @Test
    void register_shouldReturnMember_whenRegistrationSucceeds() {
        when(memberRegistrationService.registerMember("Jane", "Doe", VALID_EMAIL, VALID_PASSWORD, "1234567890"))
                .thenReturn(admin);

        Member registeredMember = jwtAuthService.register("Jane", "Doe", VALID_EMAIL, VALID_PASSWORD, "1234567890");

        assertThat(registeredMember).isSameAs(admin);
        verify(memberRegistrationService)
                .registerMember("Jane", "Doe", VALID_EMAIL, VALID_PASSWORD, "1234567890");
    }

    @Test
    void register_shouldPropagateEmailAlreadyInUseException_whenEmailIsAlreadyRegistered() {
        when(memberRegistrationService.registerMember("Jane", "Doe", VALID_EMAIL, VALID_PASSWORD, "1234567890"))
                .thenThrow(new EmailAlreadyInUseException());

        assertThatThrownBy(() -> jwtAuthService.register("Jane", "Doe", VALID_EMAIL, VALID_PASSWORD, "1234567890"))
                .isInstanceOf(EmailAlreadyInUseException.class);
    }

    @Test
    void getAuthorizationToken_shouldReturnToken_whenCredentialsAreValid() {
        when(authenticationService.authenticate(VALID_EMAIL, VALID_PASSWORD)).thenReturn(admin);
        when(jwtService.generateAuthToken(VALID_EMAIL, admin.getRoles())).thenReturn(TOKEN);

        String token = jwtAuthService.getAuthorizationToken(VALID_EMAIL, VALID_PASSWORD);

        assertThat(token).isEqualTo(TOKEN);
        verify(authenticationService).authenticate(VALID_EMAIL, VALID_PASSWORD);
        verify(jwtService).generateAuthToken(VALID_EMAIL, admin.getRoles());
    }

    @Test
    void getAuthorizationToken_shouldPropagateAuthenticationFailure() {
        when(authenticationService.authenticate(VALID_EMAIL, VALID_PASSWORD))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        assertThatThrownBy(() -> jwtAuthService.getAuthorizationToken(VALID_EMAIL, VALID_PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    void getAccessTokenTtlSeconds_shouldReturnJwtServiceTtlInSeconds() {
        when(jwtService.getAccessTokenTtl()).thenReturn(Duration.ofMinutes(15));

        assertThat(jwtAuthService.getAccessTokenTtlSeconds()).isEqualTo(900);
        verify(jwtService).getAccessTokenTtl();
    }
}
