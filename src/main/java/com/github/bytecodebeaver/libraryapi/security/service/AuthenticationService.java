package com.github.bytecodebeaver.libraryapi.security.service;

import com.github.bytecodebeaver.libraryapi.model.entity.Member;
import com.github.bytecodebeaver.libraryapi.repository.MemberRepository;
import com.github.bytecodebeaver.libraryapi.security.exceptions.InvalidPasswordException;
import com.github.bytecodebeaver.libraryapi.security.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Member authenticate(String email, String rawPassword) {
        Member member = memberRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if (!isPasswordValid(rawPassword, member.getPassword())) {
            throw new InvalidPasswordException();
        }
        if (!member.isActive()) {
            throw new DisabledException("User account is disabled");
        }
        return member;
    }

    public boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
