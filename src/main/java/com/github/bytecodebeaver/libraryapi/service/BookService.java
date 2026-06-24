package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.model.entity.Book;
import com.github.bytecodebeaver.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
}
