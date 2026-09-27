package com.github.bytecodebeaver.libraryapi.security.config;

import com.github.bytecodebeaver.libraryapi.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (authorization != null && authorization.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
            String token = authorization.substring(AUTHORIZATION_TOKEN_PREFIX.length());
            try {
                JwtService.JwtClaims claims = jwtService.extractClaimsFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(claims.username());
                if (!userDetails.isEnabled()) {
                    throw new DisabledException("User account is disabled");
                }
                SecurityContextHolder.getContext().setAuthentication(
                        UsernamePasswordAuthenticationToken.authenticated(
                                userDetails, null, userDetails.getAuthorities()
                        )
                );
            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException | DisabledException exception) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or inactive bearer token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}