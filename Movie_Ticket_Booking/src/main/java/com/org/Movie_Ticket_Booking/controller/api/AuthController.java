package com.org.Movie_Ticket_Booking.controller.api;

import com.org.Movie_Ticket_Booking.dto.request.UserRegister;
import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import com.org.Movie_Ticket_Booking.dto.respone.RegisterRespone;
import com.org.Movie_Ticket_Booking.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController("apiAuthController")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterRespone>> register(
            @Valid @RequestBody UserRegister request,
            HttpServletRequest httpRequest) {

        RegisterRespone respone = authService.register(request);

        return ResponseEntity.ok(
                ApiResponse.<RegisterRespone>builder()
                        .code(HttpStatus.OK.value())
                        .message("Đăng ký thành công! Vui lòng kiểm tra email để xác thực.")
                        .path(httpRequest.getRequestURI())
                        .timestamp(LocalDateTime.now().toString())
                        .result(respone)
                        .build()
        );
    }
}
