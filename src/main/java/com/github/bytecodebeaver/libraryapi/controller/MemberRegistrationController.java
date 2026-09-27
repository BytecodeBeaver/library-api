package com.github.bytecodebeaver.libraryapi.controller;

import com.github.bytecodebeaver.libraryapi.security.dto.MemberRegistrationRequestDTO;
import com.github.bytecodebeaver.libraryapi.service.MemberRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class MemberRegistrationController {
    private final MemberRegistrationService memberRegistrationService;

    @PostMapping("/member")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerMember(@Valid @RequestBody MemberRegistrationRequestDTO request) {
        memberRegistrationService.registerMember(request.firstName(), request.lastName(), request.email(), request.password(), request.phoneNumber());
    }
}
