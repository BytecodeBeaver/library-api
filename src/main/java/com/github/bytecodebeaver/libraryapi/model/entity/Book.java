package com.github.bytecodebeaver.libraryapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String isbn;

    private String title;
    private Author author;
    private Publisher publisher;
    private int publicationYear;
}
