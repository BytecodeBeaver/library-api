package com.github.bytecodebeaver.libraryapi.model.entity;

import jakarta.persistence.Entity;
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
public class Member {
    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
