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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserMapper userMapper;
    @Value("${app.verification.base-url}")
    private String verificationBaseUrl;
    private static final Long CUSTOMER_ROLE_ID = 1L;
    @Override
    public RegisterRespone register(UserRegister request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(userRepository.findByNumberPhone(request.getNumberPhone()).orElse(null));

        if (user != null) {
            if (user.getIsVerified()) {
                throw new AppException(
                        user.getEmail().equals(request.getEmail()) ?
                                ErrorCode.USER_EMAIL_EXISTS :
                                ErrorCode.USER_PHONE_EXISTS
                );
            }
            updateUserInfo(user, request);
            prepareVerification(user);
            userRepository.save(user);
            mailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken(),
                    verificationBaseUrl);
            return userMapper.toRegisterRespone(user);
        }

        User newUser = userMapper.toEntity(request);
        newUser.setIsVerified(false);
        updateUserInfo(newUser, request);
        prepareVerification(newUser);
        userRepository.save(newUser);
        mailService.sendVerificationEmail(newUser.getEmail(), newUser.getVerificationToken(),
                verificationBaseUrl);

        return userMapper.toRegisterRespone(newUser);
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
