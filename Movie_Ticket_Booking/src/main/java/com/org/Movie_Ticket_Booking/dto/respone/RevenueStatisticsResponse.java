package com.org.Movie_Ticket_Booking.dto.respone;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStatisticsResponse {
    private String period;          // đổi từ year -> date cho khớp với query
    private BigDecimal totalRevenue;
    private Long bookingCount;
    private String label;
}
