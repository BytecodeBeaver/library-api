package com.github.bytecodebeaver.libraryapi.repository;

import com.github.bytecodebeaver.libraryapi.model.entity.Borrow;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    Optional<Borrow> findByCopy(Long id, PageRequest pageRequest);
}
