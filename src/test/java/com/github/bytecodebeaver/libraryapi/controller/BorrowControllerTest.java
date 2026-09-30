package com.github.bytecodebeaver.libraryapi.controller;

import com.github.bytecodebeaver.libraryapi.model.dto.BorrowRequestDTO;
import com.github.bytecodebeaver.libraryapi.model.entity.Borrow;
import com.github.bytecodebeaver.libraryapi.service.BorrowService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowControllerTest {
    @Mock
    private BorrowService borrowService;

    private BorrowController controller;

    @BeforeEach
    void setUp() {
        controller = new BorrowController(borrowService);
    }

    @Test
    void createBorrow_delegatesAndReturnsCreatedBorrow() {
        BorrowRequestDTO request = new BorrowRequestDTO(2L, 3L, LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        Borrow borrow = new Borrow();
        when(borrowService.createBorrow(request)).thenReturn(borrow);

        assertThat(controller.createBorrow(request)).isSameAs(borrow);
        verify(borrowService).createBorrow(request);
    }

    @Test
    void getBorrowById_delegatesIdAndReturnsBorrow() {
        Borrow borrow = new Borrow();
        when(borrowService.getBorrowById(5L)).thenReturn(borrow);

        assertThat(controller.getBorrowById(5L)).isSameAs(borrow);
    }

    @Test
    void getBorrowsPaged_delegatesPageRequest() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Borrow> page = new PageImpl<>(List.of(new Borrow()));
        when(borrowService.getBorrowsPaged(pageable)).thenReturn(page);

        assertThat(controller.getBorrowsPaged(pageable)).isSameAs(page);
    }

    @Test
    void updateBorrow_delegatesAndReturnsUpdatedBorrow() {
        Borrow borrow = new Borrow();
        when(borrowService.updateBorrow(borrow)).thenReturn(borrow);

        assertThat(controller.updateBorrow(borrow)).isSameAs(borrow);
        verify(borrowService).updateBorrow(borrow);
    }

    @Test
    void deleteBorrowById_delegatesIdAndDeclaresNoContentStatus() throws NoSuchMethodException {
        controller.deleteBorrowById(5L);

        verify(borrowService).softDeleteBorrowById(5L);
        assertThat(BorrowController.class.getMethod("deleteBorrowById", Long.class)
                .getAnnotation(ResponseStatus.class).value()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
