package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.model.dto.BorrowRequestDTO;
import com.github.bytecodebeaver.libraryapi.model.entity.Borrow;
import com.github.bytecodebeaver.libraryapi.repository.BorrowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {
    @Mock
    private BorrowRepository borrowRepository;
    @Mock
    private CopyService copyService;
    @Mock
    private MemberService memberService;

    private BorrowService borrowService;

    @BeforeEach
    void setUp() {
        borrowService = new BorrowService(borrowRepository, copyService, memberService);
    }

    @Test
    void createBorrow_mapsRequestToBorrow() {
        LocalDateTime borrowedAt = LocalDateTime.of(2026, 9, 1, 10, 0);
        LocalDateTime dueAt = borrowedAt.plusDays(14);
        Borrow result = borrowService.createBorrow(new BorrowRequestDTO(12L, 34L, borrowedAt, dueAt));

        assertThat(result.getCopy().getId()).isEqualTo(12L);
        assertThat(result.getMember().getId()).isEqualTo(34L);
        assertThat(result.getBorrowDate()).isEqualTo(borrowedAt);
        assertThat(result.getExpectedReturnDate()).isEqualTo(dueAt);
    }

    @Test
    void isCopyBorrowed_checksWhetherActiveBorrowCountIsPositive() {
        when(borrowRepository.countActiveBorrows(12L)).thenReturn(1L, 0L);

        assertThat(borrowService.isCopyBorrowed(12L)).isTrue();
        assertThat(borrowService.isCopyBorrowed(12L)).isFalse();
        verify(borrowRepository, org.mockito.Mockito.times(2)).countActiveBorrows(12L);
    }

    @Test
    void getBorrowById_returnsBorrowWhenPresent() {
        Borrow borrow = new Borrow();
        when(borrowRepository.findById(4L)).thenReturn(Optional.of(borrow));

        assertThat(borrowService.getBorrowById(4L)).isSameAs(borrow);
    }

    @Test
    void getBorrowById_throwsWhenMissing() {
        when(borrowRepository.findById(4L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowService.getBorrowById(4L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Borrow not found with id: 4");
    }

    @Test
    void getBorrowsPaged_delegatesToRepository() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Borrow> page = new PageImpl<>(List.of(new Borrow()));
        when(borrowRepository.findAll(pageable)).thenReturn(page);

        assertThat(borrowService.getBorrowsPaged(pageable)).isSameAs(page);
    }

    @Test
    void updateBorrow_savesAndReturnsBorrow() {
        Borrow borrow = new Borrow();
        when(borrowRepository.save(borrow)).thenReturn(borrow);

        assertThat(borrowService.updateBorrow(borrow)).isSameAs(borrow);
        verify(borrowRepository).save(borrow);
    }

    @Test
    void softDeleteBorrowById_loadsAndSavesBorrow() {
        Borrow borrow = new Borrow();
        when(borrowRepository.findById(4L)).thenReturn(Optional.of(borrow));

        borrowService.softDeleteBorrowById(4L);

        verify(borrowRepository).save(borrow);
    }
}
