package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.repository.BookCopyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CopyService {
    private final BookCopyRepository bookCopyRepository;

}
