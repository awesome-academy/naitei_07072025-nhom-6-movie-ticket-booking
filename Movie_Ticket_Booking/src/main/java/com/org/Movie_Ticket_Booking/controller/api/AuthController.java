package com.org.Movie_Ticket_Booking.controller.api;

import com.org.Movie_Ticket_Booking.dto.request.UserRegister;
import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import com.org.Movie_Ticket_Booking.dto.respone.RegisterRespone;
import com.org.Movie_Ticket_Booking.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Locale;

@RestController("apiAuthController")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MessageSource messageSource;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterRespone>> register(
            @Valid @RequestBody UserRegister request,
            HttpServletRequest httpRequest,
            Locale locale) {

        RegisterRespone respone = authService.register(request);
        String registerSuccess = messageSource.getMessage(
                "register.success",
                null,
                locale
        );

        return ResponseEntity.ok(
                ApiResponse.<RegisterRespone>builder()
                        .code(HttpStatus.OK.value())
                        .message(registerSuccess)
                        .path(httpRequest.getRequestURI())
                        .timestamp(LocalDateTime.now().toString())
                        .result(respone)
                        .build()
        );
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify(
            @RequestParam String token, HttpServletRequest httpRequest, Locale locale
    ){
        authService.verifyAccount(token);
        String verifySuccess = messageSource.getMessage(
                "account.verify.success",
                null,
                locale
        );
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .message(verifySuccess)
                        .path(httpRequest.getRequestURI())
                        .timestamp(LocalDateTime.now().toString())
                        .result(verifySuccess)
                        .build()
        );
    }
}
