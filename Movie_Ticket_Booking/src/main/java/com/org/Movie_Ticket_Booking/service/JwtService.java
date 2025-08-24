package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.constants.JwtClaims;
import com.org.Movie_Ticket_Booking.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access.expiration:3600000}") // 1 hour
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration:604800000}") // 7 days
    private long refreshTokenExpiration;

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration, "access");
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, "refresh");
    }

    private String generateToken(User user, long expiration, String tokenType) {
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            log.warn("Cannot generate JWT: user or email is null/empty");
            return null;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaims.USER_ID, user.getId());
        claims.put(JwtClaims.EMAIL, user.getEmail());
        claims.put(JwtClaims.NAME, user.getName());
        claims.put(JwtClaims.TOKEN_TYPE, tokenType);
        claims.put(JwtClaims.ROLES, user.getRoles() != null
                ? user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet())
                : Collections.emptySet());

        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("Failed to generate JWT: ", e);
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            if (token == null || token.isEmpty()) return false;
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: ", e);
            return false;
        }
    }

    public boolean validateToken(String token, String email) {
        try {
            if (token == null || token.isEmpty() || email == null || email.isEmpty()) return false;
            Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
            String tokenEmail = claims.getSubject();
            return email.equals(tokenEmail) && claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token for email {}: ", email, e);
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            if (token == null || token.isEmpty()) return null;
            Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Cannot parse JWT to get email: ", e);
            return null;
        }
    }

    public Set<String> getRolesFromToken(String token) {
        try {
            if (token == null || token.isEmpty()) return Collections.emptySet();
            Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof Set) return (Set<String>) rolesObj;
            if (rolesObj instanceof Iterable) {
                Set<String> roles = new HashSet<>();
                ((Iterable<?>) rolesObj).forEach(r -> roles.add(r.toString()));
                return roles;
            }
            return Collections.emptySet();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Cannot parse JWT to get roles: ", e);
            return Collections.emptySet();
        }
    }

    private SecretKey getSigningKey() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalStateException("JWT secret is null or empty");
        }
        byte[] keyBytes = jwtSecret.getBytes();
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret length must be at least 32 bytes");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
