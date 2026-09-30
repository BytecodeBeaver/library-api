package com.github.bytecodebeaver.libraryapi.security.service;

import com.github.bytecodebeaver.libraryapi.model.entity.Member;
import com.github.bytecodebeaver.libraryapi.repository.MemberRepository;
import com.github.bytecodebeaver.libraryapi.security.exceptions.InvalidPasswordException;
import com.github.bytecodebeaver.libraryapi.security.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private static final String VALID_EMAIL = "admin@localhost";
    private static final String VALID_PASSWORD = "admin";
    private static final String INVALID_EMAIL = "invalid@localhost";

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private MemberRepository memberRepository;

    private AuthenticationService authenticationService;
    private Member admin;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(passwordEncoder, memberRepository);

        admin = new Member();
        admin.setEmail(VALID_EMAIL);
        admin.setPassword(passwordEncoder.encode(VALID_PASSWORD));
        admin.setActive(true);
    }

    @Test
    void authenticate_shouldReturnMember_whenEmailAndPasswordAreValid() {
        when(memberRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(admin));

        Member authenticated = authenticationService.authenticate(VALID_EMAIL, VALID_PASSWORD);

        assertThat(authenticated).isEqualTo(admin);
        verify(memberRepository, times(1)).findByEmail(eq(VALID_EMAIL));
    }

    @Test
    void authenticate_shouldThrowInvalidPasswordException_whenPasswordDoesNotMatch() {
        when(memberRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> authenticationService.authenticate(VALID_EMAIL, "wrong-password"))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void authenticate_shouldThrowUserNotFoundException_whenEmailDoesNotExist() {
        when(memberRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.authenticate(INVALID_EMAIL, VALID_PASSWORD))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void authenticate_shouldThrowDisabledException_whenAccountIsInactive() {
        admin.setActive(false);
        when(memberRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> authenticationService.authenticate(VALID_EMAIL, VALID_PASSWORD))
                .isInstanceOf(DisabledException.class);
    }

    @Test
    void authenticate_shouldNotQueryRepository_whenCalledWithDifferentEmail() {
        when(memberRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.authenticate(INVALID_EMAIL, VALID_PASSWORD))
                .isInstanceOf(UserNotFoundException.class);

        verify(memberRepository, never()).findByEmail(VALID_EMAIL);
    }

    @Test
    void isPasswordValid_shouldReturnTrue_whenRawPasswordMatchesEncodedPassword() {
        String encoded = passwordEncoder.encode(VALID_PASSWORD);

        assertThat(authenticationService.isPasswordValid(VALID_PASSWORD, encoded)).isTrue();
    }

    @Test
    void isPasswordValid_shouldReturnFalse_whenRawPasswordDoesNotMatchEncodedPassword() {
        String encoded = passwordEncoder.encode(VALID_PASSWORD);

        assertThat(authenticationService.isPasswordValid("wrong-password", encoded)).isFalse();
    }
}
