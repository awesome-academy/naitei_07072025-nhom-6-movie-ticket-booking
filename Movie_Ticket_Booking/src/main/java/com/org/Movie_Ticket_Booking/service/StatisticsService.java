package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.dto.respone.MovieStatisticsResponse;
import com.org.Movie_Ticket_Booking.dto.respone.RevenueStatisticsResponse;
import com.org.Movie_Ticket_Booking.dto.respone.TimeSlotStatisticsResponse;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {
    List<MovieStatisticsResponse> getMovieStatisticsByManager(Long managerId, Long cinemaId, LocalDate startDate, LocalDate endDate);
    List<TimeSlotStatisticsResponse> getTimeSlotStatisticsByManager(Long managerId, Long cinemaId, LocalDate startDate, LocalDate endDate);
    List<RevenueStatisticsResponse> getRevenueStatisticsByManager(Long managerId, Long cinemaId, String period, LocalDate startDate, LocalDate endDate);
}
