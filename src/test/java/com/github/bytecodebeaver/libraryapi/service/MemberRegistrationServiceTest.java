package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.model.entity.Member;
import com.github.bytecodebeaver.libraryapi.repository.MemberRepository;
import com.github.bytecodebeaver.libraryapi.security.exceptions.EmailAlreadyInUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberRegistrationServiceTest {

    private static final String VALID_EMAIL = "jane@localhost";

    @Mock
    private MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MemberRegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new MemberRegistrationService(memberRepository, passwordEncoder);
    }

    @Test
    void registerMember_should_throw_exception_when_email_is_already_in_use() {
        when(memberRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(new Member()));

        assertThatThrownBy(() -> registrationService.registerMember(
                "Jane", "Doe", VALID_EMAIL, "password", "1234567890"))
                .isInstanceOf(EmailAlreadyInUseException.class);
    }

    @Test
    void registerMember_should_return_member_with_same_data() {
        Member registeredMember = registrationService
                .registerMember(
                        "Jane",
                        "Doe",
                        VALID_EMAIL,
                        "password",
                        "1234567890"
                );
        assertThat(registeredMember.getFirstName()).isEqualTo("Jane");
        assertThat(registeredMember.getLastName()).isEqualTo("Doe");
        assertThat(registeredMember.getEmail()).isEqualTo(VALID_EMAIL);
        assertThat(passwordEncoder.matches("password", registeredMember.getPassword())).isTrue();
        assertThat(registeredMember.getPhoneNumber()).isEqualTo("1234567890");
        verify(memberRepository).save(registeredMember);
    }
}
