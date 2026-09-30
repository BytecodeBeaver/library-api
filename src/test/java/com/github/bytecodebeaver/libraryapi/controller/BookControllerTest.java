package com.github.bytecodebeaver.libraryapi.controller;

import com.github.bytecodebeaver.libraryapi.model.entity.Book;
import com.github.bytecodebeaver.libraryapi.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    @Mock
    private BookService bookService;

    private BookController controller;

    @BeforeEach
    void setUp() {
        controller = new BookController(bookService);
    }

    @Test
    void createBook_delegatesAndReturnsCreatedBook() {
        Book book = new Book("9780000000001", "A Book", null, null, 2020);
        when(bookService.createNewBook(book)).thenReturn(book);

        assertThat(controller.createBook(book)).isSameAs(book);
        verify(bookService).createNewBook(book);
    }

    @Test
    void getBookById_delegatesIsbnAndReturnsBook() {
        Book book = new Book("9780000000001", "A Book", null, null, 2020);
        when(bookService.getBookByIsbn(book.getIsbn())).thenReturn(book);

        assertThat(controller.getBookById(book.getIsbn())).isSameAs(book);
    }

    @Test
    void getBooksPaged_delegatesPageRequest() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<>(List.of(new Book("9780000000001", "A Book", null, null, 2020)));
        when(bookService.getBooks(pageable)).thenReturn(page);

        assertThat(controller.getBooksPaged(pageable)).isSameAs(page);
    }

    @Test
    void updateBook_delegatesAndReturnsUpdatedBook() {
        Book book = new Book("9780000000001", "A Book", null, null, 2020);
        when(bookService.updateBook(book)).thenReturn(book);

        assertThat(controller.updateBook(book)).isSameAs(book);
        verify(bookService).updateBook(book);
    }

    @Test
    void deleteBookById_delegatesIsbnAndDeclaresNoContentStatus() throws NoSuchMethodException {
        controller.deleteBookById("9780000000001");

        verify(bookService).deleteBookByIsbn("9780000000001");
        assertThat(BookController.class.getMethod("deleteBookById", String.class)
                .getAnnotation(ResponseStatus.class).value()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
