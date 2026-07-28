package com.github.bytecodebeaver.libraryapi.repository;

import com.github.bytecodebeaver.libraryapi.model.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    @Query("""
        SELECT COUNT(b) FROM Borrow b
        WHERE b.copy.id = :id AND b.returnDate IS NULL
        ORDER BY b.borrowDate DESC
        LIMIT 1
""")
    long countActiveBorrows(Long copyId);
}
