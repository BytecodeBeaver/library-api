package com.github.bytecodebeaver.libraryapi.controller;

import com.github.bytecodebeaver.libraryapi.model.entity.Book;
import com.github.bytecodebeaver.libraryapi.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createNewBook(book);
    }

    @GetMapping("/{isbn}")
    public Book getBookById(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @GetMapping
    public Page<Book> getBooksPaged(Pageable pageable) {
        return bookService.getBooks(pageable);
    }

    @PatchMapping
    public Book updateBook(@RequestBody Book book) {
        return bookService.updateBook(book);
    }

    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookById(@PathVariable String isbn) {
        bookService.deleteBookByIsbn(isbn);
    }
}
