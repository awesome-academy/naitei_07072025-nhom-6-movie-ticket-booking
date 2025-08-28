package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.dto.respone.MovieStatisticsResponse;
import com.org.Movie_Ticket_Booking.dto.respone.RevenueStatisticsResponse;
import com.org.Movie_Ticket_Booking.dto.respone.TimeSlotStatisticsResponse;
import com.org.Movie_Ticket_Booking.repository.BookingRepository;
import com.org.Movie_Ticket_Booking.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final BookingRepository bookingRepository;

    @Autowired
    public StatisticsServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<MovieStatisticsResponse> getMovieStatisticsByManager(Long managerId, Long cinemaId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        if (cinemaId != null) {
            return bookingRepository.getMovieStatisticsByCinema(managerId, cinemaId, start, end);
        } else {
            return bookingRepository.getMovieStatisticsByManager(managerId, start, end);
        }
    }

    @Override
    public List<TimeSlotStatisticsResponse> getTimeSlotStatisticsByManager(Long managerId, Long cinemaId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<TimeSlotStatisticsResponse> stats;
        if (cinemaId != null) {
            stats = bookingRepository.getTimeSlotStatisticsByCinema(managerId, cinemaId, start, end);
        } else {
            stats = bookingRepository.getTimeSlotStatisticsByManager(managerId, start, end);
        }

        long totalBookings = stats.stream()
                .mapToLong(TimeSlotStatisticsResponse::getTotalBookings)
                .sum();

        BigDecimal totalRevenue = stats.stream()
                .map(TimeSlotStatisticsResponse::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (TimeSlotStatisticsResponse stat : stats) {
            double bookingPercent = totalBookings > 0
                    ? (stat.getTotalBookings() * 100.0 / totalBookings)
                    : 0.0;

            double revenuePercent = totalRevenue.compareTo(BigDecimal.ZERO) > 0
                    ? stat.getTotalRevenue().doubleValue() * 100.0 / totalRevenue.doubleValue()
                    : 0.0;

            stat.setBookingPercentage(bookingPercent);
            stat.setRevenuePercentage(revenuePercent);
        }

        return stats;
    }

    @Override
    public List<RevenueStatisticsResponse> getRevenueStatisticsByManager(Long managerId, Long cinemaId, String period, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        if (cinemaId != null) {
            switch (period.toLowerCase()) {
                case "day":
                    return bookingRepository.getDailyRevenueStatisticsByCinema(managerId, cinemaId, start, end);
                case "month":
                    return bookingRepository.getMonthlyRevenueStatisticsByCinema(managerId, cinemaId, start, end);
                case "year":
                    return bookingRepository.getYearlyRevenueStatisticsByCinema(managerId, cinemaId, start, end);
                default:
                    return bookingRepository.getDailyRevenueStatisticsByCinema(managerId, cinemaId, start, end);
            }
        } else {
            switch (period.toLowerCase()) {
                case "day":
                    return bookingRepository.getDailyRevenueStatisticsByManager(managerId, start, end);
                case "month":
                    return bookingRepository.getMonthlyRevenueStatisticsByManager(managerId, start, end);
                case "year":
                    return bookingRepository.getYearlyRevenueStatisticsByManager(managerId, start, end);
                default:
                    return bookingRepository.getDailyRevenueStatisticsByManager(managerId, start, end);
            }
        }
    }
}
