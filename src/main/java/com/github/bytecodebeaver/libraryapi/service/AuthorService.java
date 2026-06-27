package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.exceptions.ResourceAlreadyExistsException;
import com.github.bytecodebeaver.libraryapi.exceptions.ResourceNotFoundException;
import com.github.bytecodebeaver.libraryapi.model.entity.Author;
import com.github.bytecodebeaver.libraryapi.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Author createNewAuthor(Author author) {
        authorRepository.findById(author.getId()).ifPresent((existingAuthor) -> {
            throw new ResourceAlreadyExistsException(author.getId().toString(), "AUTHOR_ALREADY_EXISTS");
        });
        return authorRepository.save(author);
    }

    public Author getAuthorById(Long id) {
        return authorRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString(), "AUTHOR_NOT_FOUND"));
    }

    public Page<Author> getAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public Author updateAuthor(Author author) {
        // Avoid creating a author if there is already on with such id
        return authorRepository.save(
                authorRepository.findById(author.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(author.getId().toString(), "AUTHOR_NOT_FOUND"))
        );
    }

    public void deleteAuthorById(Long id) {
        authorRepository.delete(
                authorRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(id.toString(), "AUTHOR_NOT_FOUND"))
        );
    }
}
