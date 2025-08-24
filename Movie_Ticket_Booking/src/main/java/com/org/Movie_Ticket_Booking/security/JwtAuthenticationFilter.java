package com.org.Movie_Ticket_Booking.security;

import com.org.Movie_Ticket_Booking.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = extractToken(authHeader);
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String userEmail = jwtService.getEmailFromToken(jwt);

            // Nếu không có email hoặc đã có Authentication thì bỏ qua
            if (userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Validate token
            if (!jwtService.validateToken(jwt, userEmail)) {
                unauthorized(response, "Invalid JWT token");
                return;
            }

            Set<String> roles = jwtService.getRolesFromToken(jwt);
            Set<SimpleGrantedAuthority> authorities = (roles == null ? Collections.<String>emptySet() : roles)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            log.error("Cannot set user authentication: ", e);
            unauthorized(response, "Authentication failed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(String header) {
        if (header.length() <= 7) {
            log.warn("Authorization header too short to contain JWT: {}", header);
            return null;
        }
        String token = header.substring(7).trim();
        if (token.isEmpty()) {
            log.warn("JWT is empty after Bearer prefix");
            return null;
        }
        return token;
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
