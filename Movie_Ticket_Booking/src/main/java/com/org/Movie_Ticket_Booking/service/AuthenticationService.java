package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.dto.request.AuthenticationRequest;
import com.org.Movie_Ticket_Booking.dto.request.RegisterRequest;
import com.org.Movie_Ticket_Booking.dto.response.AuthenticationResponse;
import com.org.Movie_Ticket_Booking.entity.Role;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.repository.RoleRepository;
import com.org.Movie_Ticket_Booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;  // Lưu/truy vấn User
    private final RoleRepository roleRepository;  // Lấy thông tin Role
    private final PasswordEncoder passwordEncoder;  // Mã hóa mật khẩu
    private final JwtService jwtService;  // Xử lý JWT
    private final AuthenticationManager authenticationManager;

    // Xử lý đăng ký user mới
    public AuthenticationResponse register(RegisterRequest request) {
        Role userRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Error: Role 'CUSTOMER' is not found in the database."));

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .numberPhone(request.getNumberPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .isVerified(0) // Mặc định tài khoản chưa được xác thực
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // Xử lý đăng nhập
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
