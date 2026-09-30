package com.github.bytecodebeaver.libraryapi.security.service;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {
    private static final String SECRET = Base64.getEncoder().encodeToString(new byte[32]);

    @Test
    void generateAuthToken_andExtractClaims_roundTripSubjectRolesAndExpiry() {
        Duration ttl = Duration.ofMinutes(15);
        JwtService jwtService = new JwtService(SECRET, ttl);

        Instant beforeGeneration = Instant.now();
        String token = jwtService.generateAuthToken("reader@example.com", List.of("ROLE_MEMBER"));
        JwtService.JwtClaims claims = jwtService.extractClaimsFromToken(token);
        Instant afterGeneration = Instant.now();

        assertThat(claims.username()).isEqualTo("reader@example.com");
        assertThat(claims.roles()).containsExactly("ROLE_MEMBER");
        assertThat(claims.expiresAt()).isBetween(
                beforeGeneration.truncatedTo(ChronoUnit.SECONDS).plus(ttl),
                afterGeneration.plus(ttl).plusSeconds(1));
        assertThat(jwtService.getAccessTokenTtl()).isEqualTo(ttl);
    }

    @Test
    void extractClaims_rejectsTokenSignedByAnotherKey() {
        JwtService issuer = new JwtService(SECRET, Duration.ofMinutes(5));
        String token = issuer.generateAuthToken("reader@example.com", List.of("ROLE_MEMBER"));
        byte[] differentKey = new byte[32];
        differentKey[0] = 1;
        String otherSecret = Base64.getEncoder().encodeToString(differentKey);
        JwtService verifier = new JwtService(otherSecret, Duration.ofMinutes(5));

        assertThatThrownBy(() -> verifier.extractClaimsFromToken(token))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void constructor_rejectsNonPositiveTtl() {
        assertThatThrownBy(() -> new JwtService(SECRET, Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("JWT access token TTL must be positive");
        assertThatThrownBy(() -> new JwtService(SECRET, Duration.ofSeconds(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("JWT access token TTL must be positive");
    }
}
