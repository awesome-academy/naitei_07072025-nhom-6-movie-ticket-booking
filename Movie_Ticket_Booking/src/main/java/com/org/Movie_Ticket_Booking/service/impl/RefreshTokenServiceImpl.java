package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.RefreshToken;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.repository.RefreshTokenRepository;
import com.org.Movie_Ticket_Booking.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createOrUpdateRefreshToken(User user, String tokenValue, Instant expiryDate) {
        return refreshTokenRepository.findByUserId(user.getId())
                .map(existingToken -> {
                    existingToken.setToken(tokenValue);
                    existingToken.setExpiryDate(expiryDate);
                    log.info("Updated existing refresh token for userId [{}]", user.getId());
                    return refreshTokenRepository.save(existingToken);
                })
                .orElseGet(() -> {
                    RefreshToken newToken = RefreshToken.builder()
                            .user(user)
                            .token(tokenValue)
                            .expiryDate(expiryDate)
                            .build();
                    log.info("Created new refresh token for userId [{}]", user.getId());
                    return refreshTokenRepository.save(newToken);
                });
    }

    @Override
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUserId(user.getId());
        log.info("Deleted refresh token for userId [{}]", user.getId());
    }
    @Override
    public boolean existsByUser(User user) {
        return refreshTokenRepository.findByUserId(user.getId()).isPresent();
    }
}
