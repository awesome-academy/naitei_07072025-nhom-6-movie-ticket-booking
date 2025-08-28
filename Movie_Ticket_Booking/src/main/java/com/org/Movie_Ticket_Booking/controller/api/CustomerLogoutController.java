package com.org.Movie_Ticket_Booking.controller.api;

import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class CustomerLogoutController {

    private final LogoutService logoutService;
    private final MessageSource messageSource;

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        ApiResponse<String> response = new ApiResponse<>();

        try {
            // Lấy token từ header Authorization
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Logout attempt without valid Authorization header");
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            String token = extractToken(authHeader);
            if (token == null) {
                log.warn("Invalid token format in Authorization header");
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            logoutService.logout(token);

            response.setCode(HttpStatus.OK.value());
            response.setMessage(getMessage("auth.logout.success"));
            response.setResult("Đăng xuất thành công");
            response.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            log.info("User logged out successfully");
            return ResponseEntity.ok(response);

        } catch (AppException ex) {
            log.error("Logout failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error during logout: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.LOGOUT_FAILED);
        }
    }

    private String extractToken(String authHeader) {
        if (authHeader.length() <= 7) {
            log.warn("Authorization header too short to contain JWT: {}", authHeader);
            return null;
        }
        String token = authHeader.substring(7).trim();
        if (token.isEmpty()) {
            log.warn("JWT is empty after Bearer prefix");
            return null;
        }
        return token;
    }

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(key, null, locale);
        } catch (Exception e) {
            return key;
        }
    }
}
