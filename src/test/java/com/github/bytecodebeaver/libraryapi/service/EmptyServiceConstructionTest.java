package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.repository.BookCopyRepository;
import com.github.bytecodebeaver.libraryapi.repository.MemberRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class EmptyServiceConstructionTest {
    @Test
    void copyService_canBeCreatedWithItsRepositoryDependency() {
        assertThat(new CopyService(mock(BookCopyRepository.class))).isNotNull();
    }

    @Test
    void memberService_canBeCreatedWithItsRepositoryDependency() {
        assertThat(new MemberService(mock(MemberRepository.class))).isNotNull();
    }
}
