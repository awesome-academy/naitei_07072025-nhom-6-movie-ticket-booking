package com.org.Movie_Ticket_Booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class BookingRequest {
    private Long showtimeId;
    private Long userId;
    private Long promotionId; // Tùy chọn
    private Set<Long> seatIds;
}
