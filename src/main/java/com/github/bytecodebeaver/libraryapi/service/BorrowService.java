package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.model.dto.BorrowRequestDTO;
import com.github.bytecodebeaver.libraryapi.model.entity.Borrow;
import com.github.bytecodebeaver.libraryapi.model.entity.Copy;
import com.github.bytecodebeaver.libraryapi.model.entity.Member;
import com.github.bytecodebeaver.libraryapi.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final CopyService copyService;
    private final MemberService memberService;

    public Borrow createBorrow(BorrowRequestDTO borrowData) {
        Copy copy = new Copy();
        copy.setId(borrowData.bookCopyId());

        Member member = new Member();
        member.setId(borrowData.memberId());

        LocalDateTime now = LocalDateTime.now();

        Borrow borrow = new Borrow();
        borrow.setMember(member);
        borrow.setCopy(copy);
        borrow.setBorrowDate(borrowData.borrowDate());
        borrow.setExpectedReturnDate(borrowData.expectedReturnDate());

        return borrow;
    }

    

    public boolean isCopyBorrowed(Long id) {
        return borrowRepository.countActiveBorrows(id) > 0;
    }

    public Borrow getBorrowById(Long id) {
        return borrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow not found with id: " + id));
    }

    public Page<Borrow> getBorrowsPaged(Pageable pageable) {
        return borrowRepository.findAll(pageable);
    }

    public Borrow updateBorrow(Borrow borrow) {
        return borrowRepository.save(borrow);
    }

    public void softDeleteBorrowById(Long id) {
        Borrow borrow = getBorrowById(id);
        // TODO: Implement soft delete logic, e.g., set a 'deleted' flag or similar
        borrowRepository.save(borrow);
    }
}
