package com.github.bytecodebeaver.libraryapi.controller;

import com.github.bytecodebeaver.libraryapi.model.entity.Author;
import com.github.bytecodebeaver.libraryapi.service.AuthorService;
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
class AuthorControllerTest {
    @Mock
    private AuthorService authorService;

    private AuthorController controller;

    @BeforeEach
    void setUp() {
        controller = new AuthorController(authorService);
    }

    @Test
    void createAuthor_delegatesAndReturnsCreatedAuthor() {
        Author author = new Author(1L, "Ada", "Lovelace");
        when(authorService.createNewAuthor(author)).thenReturn(author);

        assertThat(controller.createAuthor(author)).isSameAs(author);
        verify(authorService).createNewAuthor(author);
    }

    @Test
    void getAuthorById_delegatesIdAndReturnsAuthor() {
        Author author = new Author(1L, "Ada", "Lovelace");
        when(authorService.getAuthorById(1L)).thenReturn(author);

        assertThat(controller.getAuthorById(1L)).isSameAs(author);
    }

    @Test
    void getAuthorsPaged_delegatesPageRequest() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Author> page = new PageImpl<>(List.of(new Author(1L, "Ada", "Lovelace")));
        when(authorService.getAuthors(pageable)).thenReturn(page);

        assertThat(controller.getAuthorsPaged(pageable)).isSameAs(page);
    }

    @Test
    void updateAuthor_delegatesAndReturnsUpdatedAuthor() {
        Author author = new Author(1L, "Ada", "Lovelace");
        when(authorService.updateAuthor(author)).thenReturn(author);

        assertThat(controller.updateAuthor(author)).isSameAs(author);
        verify(authorService).updateAuthor(author);
    }

    @Test
    void deleteAuthorById_delegatesIdAndDeclaresNoContentStatus() throws NoSuchMethodException {
        controller.deleteAuthorById(1L);

        verify(authorService).deleteAuthorById(1L);
        assertThat(AuthorController.class.getMethod("deleteAuthorById", Long.class)
                .getAnnotation(ResponseStatus.class).value()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
