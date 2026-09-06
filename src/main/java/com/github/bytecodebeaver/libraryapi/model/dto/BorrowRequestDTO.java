package com.github.bytecodebeaver.libraryapi.model.dto;

import java.time.LocalDateTime;

public record BorrowRequestDTO(Long bookCopyId, Long memberId, LocalDateTime borrowDate, LocalDateTime expectedReturnDate) {

}
