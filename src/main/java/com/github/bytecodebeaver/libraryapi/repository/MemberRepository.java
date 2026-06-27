package com.github.bytecodebeaver.libraryapi.repository;

import com.github.bytecodebeaver.libraryapi.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
