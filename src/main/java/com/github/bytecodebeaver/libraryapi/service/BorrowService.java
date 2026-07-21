package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.model.dto.BorrowRequestDTO;
import com.github.bytecodebeaver.libraryapi.model.entity.Borrow;
import com.github.bytecodebeaver.libraryapi.model.entity.Copy;
import com.github.bytecodebeaver.libraryapi.model.entity.Member;
import com.github.bytecodebeaver.libraryapi.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final CopyService copyService;
    private final MemberService memberService;

    public Borrow createBorrow(BorrowRequestDTO borrowData, int forDays) {
        Copy copy = new Copy();
        copy.setId(borrowData.bookCopyId());

        Member member = new Member();
        member.setId(borrowData.memberId());

        LocalDateTime now = LocalDateTime.now();

        Borrow borrow = new Borrow();
        borrow.setMember(member);
        borrow.setCopy(copy);
        borrow.setBorrowDate(now);
        borrow.setExpectedReturnDate(now.plusDays(forDays));

        return borrow;
    }

    public isCopyBorrowed(Long id) {
        // Find borrow for copy id and get the newest one. If It's present, then check if It's expired
        // SELECT * FROM borrows WHERE copy_id = %copy_id_param AND return_date == null;
        return borrowRepository.findByCopy(id, Limit.of(1));
    }
}
