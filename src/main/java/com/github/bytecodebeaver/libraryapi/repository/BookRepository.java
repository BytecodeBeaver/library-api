package com.github.bytecodebeaver.libraryapi.repository;

import com.github.bytecodebeaver.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
