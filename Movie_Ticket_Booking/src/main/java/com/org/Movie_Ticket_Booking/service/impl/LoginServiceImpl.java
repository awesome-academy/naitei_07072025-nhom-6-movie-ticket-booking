package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.dto.request.LoginRequest;
import com.org.Movie_Ticket_Booking.dto.respone.LoginResponse;
import com.org.Movie_Ticket_Booking.entity.Role;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.UserRepository;
import com.org.Movie_Ticket_Booking.service.JwtService;
import com.org.Movie_Ticket_Booking.service.LoginService;
import com.org.Movie_Ticket_Booking.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmailOrPhone(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatch) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Instant refreshTokenExpiry = Instant.now().plusMillis(jwtService.getRefreshTokenExpiration());
        refreshTokenService.createOrUpdateRefreshToken(user, refreshToken, refreshTokenExpiry);

        return LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .roles(user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
