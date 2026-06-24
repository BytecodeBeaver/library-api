package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.exceptions.ResourceAlreadyExistsException;
import com.github.bytecodebeaver.libraryapi.exceptions.ResourceNotFoundException;
import com.github.bytecodebeaver.libraryapi.model.entity.Book;
import com.github.bytecodebeaver.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Book createNewBook(Book book) {
        bookRepository.findById(book.getIsbn()).ifPresent((existingBook) -> {
            throw new ResourceAlreadyExistsException();
        });
        return bookRepository.save(book);
    }

    public Book getBookByIsbn(String isbn) {
        return bookRepository.findById(isbn).orElseThrow(ResourceNotFoundException::new);
    }

    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book updateBook(Book book) {
        // Avoid creating a book if there is already on with such id
        return bookRepository.save(
                bookRepository.findById(book.getIsbn())
                        .orElseThrow(ResourceNotFoundException::new)
        );
    }

    public void deleteBookByIsbn(String isbn) {
        bookRepository.delete(
                bookRepository
                        .findById(isbn)
                        .orElseThrow(ResourceNotFoundException::new)
        );
    }
}
