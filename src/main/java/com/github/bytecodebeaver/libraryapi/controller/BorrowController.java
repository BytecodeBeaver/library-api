package com.github.bytecodebeaver.libraryapi.controller;

import com.github.bytecodebeaver.libraryapi.model.dto.BorrowRequestDTO;
import com.github.bytecodebeaver.libraryapi.model.entity.Borrow;
import com.github.bytecodebeaver.libraryapi.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrows")
@RequiredArgsConstructor
public class BorrowController {
    private final BorrowService borrowService;

    @PostMapping
    public Borrow createBorrow(@RequestBody BorrowRequestDTO borrowRequestDTO) {
        return borrowService.createBorrow(borrowRequestDTO);
    }

    @GetMapping("/{id}")
    public Borrow getBorrowById(@PathVariable Long id) {

        return borrowService.getBorrowById(id);
    }

    @GetMapping
    public Page<Borrow> getBorrowsPaged(Pageable pageable) {
        return borrowService.getBorrowsPaged(pageable);
    }

    @PatchMapping
    public Borrow updateBorrow(@RequestBody Borrow borrow) {
        return borrowService.updateBorrow(borrow);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBorrowById(@PathVariable Long id) {
        borrowService.softDeleteBorrowById(id);
    }
}
