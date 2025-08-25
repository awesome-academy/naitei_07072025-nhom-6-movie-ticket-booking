package com.org.Movie_Ticket_Booking.dto.respone;

import com.org.Movie_Ticket_Booking.entity.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class BookingHistory {
    private Long id;
    private LocalDateTime createdAt;
    private BookingStatus bookingStatus;
    private LocalTime startTime;
    private String movieName;
}
