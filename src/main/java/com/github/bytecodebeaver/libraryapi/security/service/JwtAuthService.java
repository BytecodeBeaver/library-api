package com.github.bytecodebeaver.libraryapi.security.service;

import com.github.bytecodebeaver.libraryapi.model.entity.Member;
import com.github.bytecodebeaver.libraryapi.service.MemberRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthService {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final MemberRegistrationService memberRegistrationService;

    public Member register(String firstName, String lastName, String email, String password, String phoneNumber) {
        return memberRegistrationService.registerMember(firstName, lastName, email, password, phoneNumber);
    }

    public String getAuthorizationToken(String username, String password) {
        Member member = authenticationService.authenticate(username, password);
        return jwtService.generateAuthToken(member.getEmail(), member.getRoles());
    }

    public long getAccessTokenTtlSeconds() {
        return jwtService.getAccessTokenTtl().toSeconds();
    }
}
