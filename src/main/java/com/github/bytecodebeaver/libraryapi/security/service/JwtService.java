package com.github.bytecodebeaver.libraryapi.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    public record JwtClaims(String username, List<String> roles, Instant expiresAt) {
    }

    private final SecretKey signingKey;
    private final Duration accessTokenTtl;

    public JwtService(
            @Value("${security.jwt.secret}") String base64Secret,
            @Value("${security.jwt.access-token-ttl:PT15M}") Duration accessTokenTtl
    ) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        if (accessTokenTtl.isZero() || accessTokenTtl.isNegative()) {
            throw new IllegalArgumentException("JWT access token TTL must be positive");
        }
        this.accessTokenTtl = accessTokenTtl;
    }

    public String generateAuthToken(String username, List<String> roles) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(accessTokenTtl);
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    public JwtClaims extractClaimsFromToken(String token) {
        var claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Object roleObject = claims.get("roles");
        if (!(roleObject instanceof List<?> roleList)
                || roleList.stream().anyMatch(role -> !(role instanceof String))) {
            throw new IllegalArgumentException("JWT roles claim must be a list of strings");
        }

        String username = claims.getSubject();
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("JWT subject must not be blank");
        }

        return new JwtClaims(
                username,
                roleList.stream().map(String.class::cast).toList(),
                claims.getExpiration().toInstant()
        );
    }

    public Duration getAccessTokenTtl() {
        return accessTokenTtl;
    }
}
