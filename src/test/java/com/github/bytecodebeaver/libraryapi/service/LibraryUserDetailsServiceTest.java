package com.github.bytecodebeaver.libraryapi.service;

import com.github.bytecodebeaver.libraryapi.model.entity.Member;
import com.github.bytecodebeaver.libraryapi.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryUserDetailsServiceTest {
    @Mock
    private MemberRepository memberRepository;

    private LibraryUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new LibraryUserDetailsService(memberRepository);
    }

    @Test
    void loadUserByUsername_mapsMemberCredentialsRolesAndEnabledState() {
        Member member = new Member();
        member.setEmail("reader@example.com");
        member.setPassword("encoded-password");
        member.setRoles(List.of("ROLE_MEMBER", "ROLE_READER"));
        member.setActive(true);
        when(memberRepository.findByEmail("reader@example.com")).thenReturn(Optional.of(member));

        UserDetails user = userDetailsService.loadUserByUsername("reader@example.com");

        assertThat(user.getUsername()).isEqualTo("reader@example.com");
        assertThat(user.getPassword()).isEqualTo("encoded-password");
        assertThat(user.getAuthorities()).extracting("authority")
                .containsExactlyInAnyOrder("ROLE_MEMBER", "ROLE_READER");
        assertThat(user.isEnabled()).isTrue();
        verify(memberRepository).findByEmail("reader@example.com");
    }

    @Test
    void loadUserByUsername_disablesInactiveMember() {
        Member member = new Member();
        member.setEmail("inactive@example.com");
        member.setPassword("encoded-password");
        member.setRoles(List.of("ROLE_MEMBER"));
        member.setActive(false);
        when(memberRepository.findByEmail("inactive@example.com")).thenReturn(Optional.of(member));

        assertThat(userDetailsService.loadUserByUsername("inactive@example.com").isEnabled()).isFalse();
    }

    @Test
    void loadUserByUsername_throwsWhenEmailDoesNotExist() {
        when(memberRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknown@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with email: unknown@example.com");
    }
}
