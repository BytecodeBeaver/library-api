package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.security.exceptions.EmailAlreadyInUseException;
import com.github.bytecodebeaver.libraryapi.model.entity.Member;
import com.github.bytecodebeaver.libraryapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberRegistrationService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member registerMember(String firstName, String lastName, String email, String password, String phoneNumber) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyInUseException();
        }

        Member member = new Member(
                null,
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password),
                phoneNumber,
                List.of("ROLE_USER"),
                false,
                true
        );

        memberRepository.save(member);

        return member;
    }
}
