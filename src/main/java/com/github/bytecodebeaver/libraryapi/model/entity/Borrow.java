package com.github.bytecodebeaver.libraryapi.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {
    public enum State {
        ACTIVE,
        FINISHED,
        OVERDUE
    }

    @Id
    private Long id;

    @Column(nullable = false)
    private Copy copy;
    @Column(nullable = false)
    private Member member;
    private LocalDateTime borrowDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime returnDate;

    public Borrow.State getState() {
        if(returnDate != null) {
            if (returnDate.isAfter(borrowDate)) {
                return Borrow.State.OVERDUE;
            } else {
                return Borrow.State.FINISHED;
            }
        }
        else {
            return Borrow.State.ACTIVE;
        }
    }
}