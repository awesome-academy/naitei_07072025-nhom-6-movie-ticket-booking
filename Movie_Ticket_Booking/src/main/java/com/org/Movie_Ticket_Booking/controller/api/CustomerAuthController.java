package com.org.Movie_Ticket_Booking.controller.api;

import com.org.Movie_Ticket_Booking.constants.RoleConstants;
import com.org.Movie_Ticket_Booking.dto.request.LoginRequest;
import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import com.org.Movie_Ticket_Booking.dto.respone.LoginResponse;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class CustomerAuthController {

    private final LoginService loginService;
    private final MessageSource messageSource;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse<LoginResponse> response = new ApiResponse<>();

        try {
            LoginResponse loginResponse = loginService.login(request);

            boolean isCustomer = loginResponse.getRoles().contains(RoleConstants.ROLE_CUSTOMER);
            if (!isCustomer) {
                log.warn("Failed login attempt for username [{}] - reason: ACCESS_DENIED", request.getUsername());
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }

            response.setCode(HttpStatus.OK.value());
            response.setMessage(getMessage("auth.login.success"));
            response.setResult(loginResponse);
            response.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            log.info("User [{}] logged in successfully with roles: {}",
                    request.getUsername(), loginResponse.getRoles());

            return ResponseEntity.ok(response);
        } catch (AppException ex) {
            log.error("Login failed for username [{}]: {}", request.getUsername(), ex.getMessage());
            throw ex;
        } catch (Exception e) {
            log.error("Unexpected error during login for username [{}]: {}", request.getUsername(), e.getMessage());
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }
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
