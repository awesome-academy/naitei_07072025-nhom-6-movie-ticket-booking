package com.org.Movie_Ticket_Booking.dto.respone;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TimeSlotStatisticsResponse {
    private String timeSlot;
    private Long totalBookings;
    private BigDecimal totalRevenue;
    private Double bookingPercentage;
    private Double revenuePercentage;
}
