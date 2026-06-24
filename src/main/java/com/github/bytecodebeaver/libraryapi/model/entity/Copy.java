package com.github.bytecodebeaver.libraryapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Copy {
    public enum Condition {
        EXCELLENT, NORMAL, BAD, TERRIBLE, UNREADABLE
    }

    @Id
    private Long id;

    private Book book;
    @Enumerated(EnumType.ORDINAL)
    private Copy.Condition condition;
}
