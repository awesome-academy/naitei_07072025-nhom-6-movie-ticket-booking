package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.dto.request.UserRegister;
import com.org.Movie_Ticket_Booking.dto.respone.RegisterRespone;
import com.org.Movie_Ticket_Booking.entity.Role;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.mapper.UserMapper;
import com.org.Movie_Ticket_Booking.repository.UserRepository;
import com.org.Movie_Ticket_Booking.service.AuthService;
import com.org.Movie_Ticket_Booking.utils.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserMapper userMapper;
    @Value("${app.verification.base-url}")
    private String verificationBaseUrl;
    private static final Long CUSTOMER_ROLE_ID = 1L;

    @Override
    public RegisterRespone register(UserRegister request) {
        Optional<User> existingUserOpt = userRepository.findByEmail(request.getEmail())
                .or(() -> userRepository.findByNumberPhone(request.getNumberPhone()));

        User user;

        if (existingUserOpt.isPresent()) {
            user  = existingUserOpt.get();
            validateDuplicate(user, request);
            updateUserInfo(user, request);
        }else {
            user = userMapper.toEntity(request);
            user.setIsVerified(false);
            updateUserInfo(user, request);
        }

        prepareVerification(user);
        userRepository.save(user);
        mailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken(),
                verificationBaseUrl);

        return userMapper.toRegisterRespone(user);
    }

    @Override
    public void verifyAccount(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.AUTH_TOKEN_INVALID));

        if (user.getVerificationExpiry().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.AUTH_TOKEN_EXPIRED);
        }

        user.setIsVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpiry(null);
        userRepository.save(user);
    }

    private void validateDuplicate(User user, UserRegister request) {
        if (Boolean.TRUE.equals(user.getIsVerified())) {
            throw new AppException(
                    user.getEmail().equals(request.getEmail())
                            ? ErrorCode.USER_EMAIL_EXISTS
                            : ErrorCode.USER_PHONE_EXISTS
            );
        }
    }

    private void updateUserInfo(User user, UserRegister request) {
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setNumberPhone(request.getNumberPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    private void prepareVerification(User user) {
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationExpiry(LocalDateTime.now().plusHours(3));

        Role customerRole = Role.builder()
                .id(CUSTOMER_ROLE_ID)
                .build();
            user.setRoles(Collections.singleton(customerRole));
    }
}
