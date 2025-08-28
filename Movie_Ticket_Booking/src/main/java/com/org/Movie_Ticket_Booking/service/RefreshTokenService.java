package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.entity.RefreshToken;
import com.org.Movie_Ticket_Booking.entity.User;

import java.time.Instant;

public interface RefreshTokenService {
    RefreshToken createOrUpdateRefreshToken(User user, String tokenValue, Instant expiryDate);
    void deleteByUser(User user);
    boolean existsByUser(User user);
}
