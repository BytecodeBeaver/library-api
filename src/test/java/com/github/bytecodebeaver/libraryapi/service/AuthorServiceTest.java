package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.exceptions.ResourceAlreadyExistsException;
import com.github.bytecodebeaver.libraryapi.exceptions.ResourceNotFoundException;
import com.github.bytecodebeaver.libraryapi.model.entity.Author;
import com.github.bytecodebeaver.libraryapi.repository.AuthorRepository;
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
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository);
    }

    @Test
    void createNewAuthor_savesWhenIdDoesNotExist() {
        Author author = new Author(3L, "Ada", "Lovelace");
        when(authorRepository.findById(3L)).thenReturn(Optional.empty());
        when(authorRepository.save(author)).thenReturn(author);

        assertThat(authorService.createNewAuthor(author)).isSameAs(author);
        verify(authorRepository).save(author);
    }

    @Test
    void createNewAuthor_throwsWhenIdAlreadyExists() {
        Author existing = new Author(3L, "Existing", "Author");
        when(authorRepository.findById(3L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> authorService.createNewAuthor(new Author(3L, "Ada", "Lovelace")))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage("AUTHOR_ALREADY_EXISTS");
        verifyNoInteractionsAfterFind();
    }

    @Test
    void getAuthorById_returnsExistingAuthor() {
        Author author = new Author(3L, "Ada", "Lovelace");
        when(authorRepository.findById(3L)).thenReturn(Optional.of(author));

        assertThat(authorService.getAuthorById(3L)).isSameAs(author);
    }

    @Test
    void getAuthorById_throwsWhenMissing() {
        when(authorRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.getAuthorById(3L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("AUTHOR_NOT_FOUND");
    }

    @Test
    void getAuthors_returnsRepositoryPage() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Author> page = new PageImpl<>(List.of(new Author(3L, "Ada", "Lovelace")));
        when(authorRepository.findAll(pageable)).thenReturn(page);

        assertThat(authorService.getAuthors(pageable)).isSameAs(page);
    }

    @Test
    void updateAuthor_savesTheRequestedAuthorAfterCheckingItExists() {
        Author persisted = new Author(3L, "Ada", "Lovelace");
        Author updated = new Author(3L, "Augusta", "King");
        when(authorRepository.findById(3L)).thenReturn(Optional.of(persisted));
        when(authorRepository.save(org.mockito.ArgumentMatchers.any(Author.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(authorService.updateAuthor(updated)).isSameAs(updated);
        verify(authorRepository).save(updated);
    }

    @Test
    void updateAuthor_throwsWhenMissing() {
        when(authorRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.updateAuthor(new Author(3L, "Ada", "Lovelace")))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("AUTHOR_NOT_FOUND");
    }

    @Test
    void deleteAuthorById_deletesFoundAuthor() {
        Author author = new Author(3L, "Ada", "Lovelace");
        when(authorRepository.findById(3L)).thenReturn(Optional.of(author));

        authorService.deleteAuthorById(3L);

        verify(authorRepository).delete(author);
    }

    @Test
    void deleteAuthorById_throwsWhenMissing() {
        when(authorRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.deleteAuthorById(3L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("AUTHOR_NOT_FOUND");
    }

    private void verifyNoInteractionsAfterFind() {
        org.mockito.Mockito.verify(authorRepository, org.mockito.Mockito.never()).save(org.mockito.ArgumentMatchers.any());
        org.mockito.Mockito.verify(authorRepository, org.mockito.Mockito.never()).delete(org.mockito.ArgumentMatchers.any());
    }
}
