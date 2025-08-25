package com.org.Movie_Ticket_Booking.controller.api;

import com.org.Movie_Ticket_Booking.dto.request.UpgradeRqDTO;
import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import com.org.Movie_Ticket_Booking.dto.respone.UpgradeRqResponse;
import com.org.Movie_Ticket_Booking.service.UpgradeRequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class UpgradeRequestController extends CustomerController{
    private final UpgradeRequestService upgradeRequestService;
    private final MessageSource messageSource;

    @PostMapping("/upgrade-request")
    public ResponseEntity<ApiResponse<UpgradeRqResponse>> upgradeRequest(
            @Valid @RequestBody UpgradeRqDTO upgradeRqDTO,
            HttpServletRequest httpServletRequest,
            Locale locale
            ){
        UpgradeRqResponse response = upgradeRequestService.createUpgradeRequest(upgradeRqDTO);
        String upgradeSuccess = messageSource.getMessage(
                "upgrade.success",
                null,
                locale
        );
        return ResponseEntity.ok(
                ApiResponse.<UpgradeRqResponse>builder()
                        .code(HttpStatus.OK.value())
                        .message(upgradeSuccess)
                        .path(httpServletRequest.getRequestURI())
                        .timestamp(LocalDateTime.now().toString())
                        .result(response)
                        .build()
        );
    }
}
