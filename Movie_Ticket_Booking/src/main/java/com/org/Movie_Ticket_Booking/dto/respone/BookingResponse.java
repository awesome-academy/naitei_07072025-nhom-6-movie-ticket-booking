package com.org.Movie_Ticket_Booking.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String movieTitle;
    private String showtimeInfo;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private String cinema;
    private String room;
    private Set<String> seatNumbers;
}
