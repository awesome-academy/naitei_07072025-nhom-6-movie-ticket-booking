package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.UserRepository;
import com.org.Movie_Ticket_Booking.service.JwtService;
import com.org.Movie_Ticket_Booking.service.LogoutService;
import com.org.Movie_Ticket_Booking.service.RefreshTokenService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public void logout(String token) {
        try {
            String userEmail;
            try {
                userEmail = jwtService.getEmailFromToken(token);
            } catch (JwtException ex) {
                log.warn("Invalid JWT token: {}", ex.getMessage());
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            if (userEmail == null) {
                log.warn("Cannot extract email from token during logout");
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            if (!jwtService.validateToken(token, userEmail)) {
                log.warn("Invalid token provided for logout: {}", userEmail);
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            log.info("Processing logout for user: {}", userEmail);

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> {
                        log.warn("User not found with email: {}", userEmail);
                        return new AppException(ErrorCode.UNAUTHORIZED);
                    });

            boolean tokenExists = refreshTokenService.existsByUser(user);
            if (!tokenExists) {
                log.warn("User [{}] has already logged out", userEmail);
                throw new AppException(ErrorCode.LOGOUT_ALREADY_DONE);
            }

            refreshTokenService.deleteByUser(user);

            log.info("Successfully deleted refresh token for user [{}] with userId [{}]", userEmail, user.getId());

        } catch (AppException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error during logout: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.LOGOUT_FAILED);
        }
    }
}
