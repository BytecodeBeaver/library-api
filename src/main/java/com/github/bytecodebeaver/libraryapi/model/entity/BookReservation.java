package com.github.bytecodebeaver.libraryapi.model.entity;

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
public class BookReservation {
    @Id
    private Long id;

    @ManyToOne
    private Member member;
    @ManyToOne
    private Book book;
    private LocalDateTime reservedAt;
}
