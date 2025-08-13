package com.org.Movie_Ticket_Booking.controller.api;

import com.org.Movie_Ticket_Booking.dto.request.AuthenticationRequest;
import com.org.Movie_Ticket_Booking.dto.request.RegisterRequest;
import com.org.Movie_Ticket_Booking.dto.response.AuthenticationResponse;
import com.org.Movie_Ticket_Booking.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    // API ĐĂNG KÝ NGƯỜI DÙNG MỚI
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        // Gọi service để xử lý đăng ký và trả về token
        return ResponseEntity.ok(service.register(request));
    }

    // API ĐĂNG NHẬP
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        // Gọi service để xác thực và trả về token
        return ResponseEntity.ok(service.authenticate(request));
    }
}
