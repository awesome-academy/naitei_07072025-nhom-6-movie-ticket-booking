package com.org.Movie_Ticket_Booking.dto.respone;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieStatisticsResponse {
    private String movieTitle;
    private Long totalTickets;
    private BigDecimal totalRevenue;
    private String movieGenre;
    private String cinemaName;
}
