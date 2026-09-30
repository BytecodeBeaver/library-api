package com.github.bytecodebeaver.libraryapi.controller;

import com.github.bytecodebeaver.libraryapi.security.dto.MemberRegistrationRequestDTO;
import com.github.bytecodebeaver.libraryapi.service.MemberRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberRegistrationControllerTest {
    @Mock
    private MemberRegistrationService registrationService;

    private MemberRegistrationController controller;

    @BeforeEach
    void setUp() {
        controller = new MemberRegistrationController(registrationService);
    }

    @Test
    void registerMember_passesAllRequestFieldsToService() {
        MemberRegistrationRequestDTO request =
                new MemberRegistrationRequestDTO("Jane", "Doe", "jane@example.com", "password123", "555-0100");

        controller.registerMember(request);

        verify(registrationService).registerMember(
                "Jane", "Doe", "jane@example.com", "password123", "555-0100");
    }

    @Test
    void registerMember_declaresCreatedStatus() throws NoSuchMethodException {
        ResponseStatus status = MemberRegistrationController.class
                .getMethod("registerMember", MemberRegistrationRequestDTO.class)
                .getAnnotation(ResponseStatus.class);

        assertThat(status.value()).isEqualTo(HttpStatus.CREATED);
    }
}
