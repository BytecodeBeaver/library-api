package com.github.bytecodebeaver.libraryapi.repository;

import com.github.bytecodebeaver.libraryapi.model.entity.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCopyRepository extends JpaRepository<Copy, Long> {
}
