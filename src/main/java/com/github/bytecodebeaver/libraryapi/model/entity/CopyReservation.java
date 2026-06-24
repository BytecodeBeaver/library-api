package com.github.bytecodebeaver.libraryapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class CopyReservation {
    @Id
    private Long id;

    private Member member;
    private Copy bookCopy;
    private LocalDateTime reservedAt;
}
