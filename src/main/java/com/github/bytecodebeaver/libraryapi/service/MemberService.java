package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


}
