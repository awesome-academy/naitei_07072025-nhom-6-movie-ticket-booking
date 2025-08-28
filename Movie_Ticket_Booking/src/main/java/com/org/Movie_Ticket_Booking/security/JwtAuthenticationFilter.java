package com.org.Movie_Ticket_Booking.security;

import com.org.Movie_Ticket_Booking.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
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
            unauthorized(response, "Invalid JWT format");
            return;
        }

        try {
            final String userEmail = jwtService.getEmailFromToken(jwt);

            if (userEmail == null) {
                unauthorized(response, "JWT missing email");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtService.validateToken(jwt, userEmail)) {
                unauthorized(response, "Invalid or expired JWT token");
                return;
            }

            Set<String> roles = jwtService.getRolesFromToken(jwt);
            Set<SimpleGrantedAuthority> authorities = (roles == null ? Set.<String>of() : roles)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (SignatureException e) {
            handleJwtException(response, "JWT signature does not match", e);
            return;
        } catch (ExpiredJwtException e) {
            handleJwtException(response, "JWT token has expired", e);
            return;
        } catch (MalformedJwtException e) {
            handleJwtException(response, "JWT token is malformed", e);
            return;
        } catch (Exception e) {
            handleJwtException(response, "Authentication failed", e);
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

    private void handleJwtException(HttpServletResponse response, String message, Exception e) throws IOException {
        log.error(message, e);
        unauthorized(response, message);
    }
}
