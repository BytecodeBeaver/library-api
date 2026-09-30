package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.exceptions.ResourceAlreadyExistsException;
import com.github.bytecodebeaver.libraryapi.exceptions.ResourceNotFoundException;
import com.github.bytecodebeaver.libraryapi.model.entity.Book;
import com.github.bytecodebeaver.libraryapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository);
    }

    @Test
    void createNewBook_savesWhenIsbnDoesNotExist() {
        Book book = new Book("9780000000001", "Analytical Engines", null, null, 1843);
        when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        assertThat(bookService.createNewBook(book)).isSameAs(book);
        verify(bookRepository).save(book);
    }

    @Test
    void createNewBook_throwsWhenIsbnAlreadyExists() {
        Book existing = new Book("9780000000001", "Existing", null, null, 2020);
        when(bookRepository.findById(existing.getIsbn())).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> bookService.createNewBook(existing))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage("BOOK_ALREADY_EXISTS");
        org.mockito.Mockito.verify(bookRepository, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void getBookByIsbn_returnsExistingBook() {
        Book book = new Book("9780000000001", "Analytical Engines", null, null, 1843);
        when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));

        assertThat(bookService.getBookByIsbn(book.getIsbn())).isSameAs(book);
    }

    @Test
    void getBookByIsbn_throwsWhenMissing() {
        when(bookRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookByIsbn("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("BOOK_NOT_FOUND");
    }

    @Test
    void getBooks_returnsRepositoryPage() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<>(List.of(new Book("9780000000001", "A Book", null, null, 2020)));
        when(bookRepository.findAll(pageable)).thenReturn(page);

        assertThat(bookService.getBooks(pageable)).isSameAs(page);
    }

    @Test
    void updateBook_savesTheRequestedBookAfterCheckingItExists() {
        Book persisted = new Book("9780000000001", "Original title", null, null, 2020);
        Book updated = new Book(persisted.getIsbn(), "Updated title", null, null, 2021);
        when(bookRepository.findById(persisted.getIsbn())).thenReturn(Optional.of(persisted));
        when(bookRepository.save(org.mockito.ArgumentMatchers.any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(bookService.updateBook(updated)).isSameAs(updated);
        verify(bookRepository).save(updated);
    }

    @Test
    void updateBook_throwsWhenMissing() {
        when(bookRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(new Book("missing", "Title", null, null, 2020)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("BOOK_NOT_FOUND");
    }

    @Test
    void deleteBookByIsbn_deletesFoundBook() {
        Book book = new Book("9780000000001", "A Book", null, null, 2020);
        when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));

        bookService.deleteBookByIsbn(book.getIsbn());

        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBookByIsbn_throwsWhenMissing() {
        when(bookRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.deleteBookByIsbn("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("BOOK_NOT_FOUND");
        org.mockito.Mockito.verify(bookRepository, org.mockito.Mockito.never())
                .delete(org.mockito.ArgumentMatchers.any());
    }
}
