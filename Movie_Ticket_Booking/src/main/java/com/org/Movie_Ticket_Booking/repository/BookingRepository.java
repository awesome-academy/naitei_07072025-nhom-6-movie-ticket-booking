package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.dto.respone.MovieStatisticsResponse;
import com.org.Movie_Ticket_Booking.dto.respone.RevenueStatisticsResponse;
import com.org.Movie_Ticket_Booking.dto.respone.TimeSlotStatisticsResponse;
import com.org.Movie_Ticket_Booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long id);


    @Query("SELECT new com.org.Movie_Ticket_Booking.dto.respone.MovieStatisticsResponse(" +
            "m.title, COUNT(DISTINCT b), SUM(bs.cost), 'Unknown', c.name) " +
            "FROM Booking b " +
            "JOIN b.bookingSeats bs " +
            "JOIN b.showtime s " +
            "JOIN s.movie m " +
            "JOIN s.room r " +
            "JOIN r.cinema c " +
            "WHERE c.manager.id = :managerId " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = com.org.Movie_Ticket_Booking.entity.enums.BookingStatus.PAID " +
            "GROUP BY m.id, m.title, c.name " +
            "ORDER BY SUM(bs.cost) DESC")
    List<MovieStatisticsResponse> getMovieStatisticsByManager(@Param("managerId") Long managerId,
                                                              @Param("startDate") LocalDateTime startDate,
                                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.org.Movie_Ticket_Booking.dto.respone.MovieStatisticsResponse(" +
            "m.title, COUNT(DISTINCT b), SUM(bs.cost), 'Unknown', c.name) " +
            "FROM Booking b " +
            "JOIN b.bookingSeats bs " +
            "JOIN b.showtime s " +
            "JOIN s.movie m " +
            "JOIN s.room r " +
            "JOIN r.cinema c " +
            "WHERE c.manager.id = :managerId " +
            "AND c.id = :cinemaId " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = com.org.Movie_Ticket_Booking.entity.enums.BookingStatus.PAID " +
            "GROUP BY m.id, m.title, c.name " +
            "ORDER BY SUM(bs.cost) DESC")
    List<MovieStatisticsResponse> getMovieStatisticsByCinema(@Param("managerId") Long managerId,
                                                             @Param("cinemaId") Long cinemaId,
                                                             @Param("startDate") LocalDateTime startDate,
                                                             @Param("endDate") LocalDateTime endDate);


    @Query("SELECT new com.org.Movie_Ticket_Booking.dto.respone.TimeSlotStatisticsResponse(" +
            "CASE " +
            "  WHEN HOUR(s.startTime) < 12 THEN 'Buổi sáng (6-12h)' " +
            "  WHEN HOUR(s.startTime) < 18 THEN 'Buổi chiều (12-18h)' " +
            "  ELSE 'Buổi tối (18-24h)' " +
            "END, " +
            "CAST(COUNT(DISTINCT b) AS LONG), " +
            "SUM(bs.cost), " +
            "CAST(0.0 AS DOUBLE), " +
            "CAST(0.0 AS DOUBLE)) " +
            "FROM Booking b " +
            "JOIN b.bookingSeats bs " +
            "JOIN b.showtime s " +
            "JOIN s.room r " +
            "JOIN r.cinema c " +
            "WHERE c.manager.id = :managerId " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = com.org.Movie_Ticket_Booking.entity.enums.BookingStatus.PAID " +
            "GROUP BY " +
            "CASE " +
            "  WHEN HOUR(s.startTime) < 12 THEN 'Buổi sáng (6-12h)' " +
            "  WHEN HOUR(s.startTime) < 18 THEN 'Buổi chiều (12-18h)' " +
            "  ELSE 'Buổi tối (18-24h)' " +
            "END " +
            "ORDER BY COUNT(DISTINCT b) DESC")
    List<TimeSlotStatisticsResponse> getTimeSlotStatisticsByManager(@Param("managerId") Long managerId,
                                                                    @Param("startDate") LocalDateTime startDate,
                                                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.org.Movie_Ticket_Booking.dto.respone.TimeSlotStatisticsResponse(" +
            "CASE " +
            "  WHEN HOUR(s.startTime) < 12 THEN 'Buổi sáng (6-12h)' " +
            "  WHEN HOUR(s.startTime) < 18 THEN 'Buổi chiều (12-18h)' " +
            "  ELSE 'Buổi tối (18-24h)' " +
            "END, " +
            "CAST(COUNT(DISTINCT b) AS LONG), " +
            "SUM(bs.cost), " +
            "CAST(0.0 AS DOUBLE), " +
            "CAST(0.0 AS DOUBLE)) " +
            "FROM Booking b " +
            "JOIN b.bookingSeats bs " +
            "JOIN b.showtime s " +
            "JOIN s.room r " +
            "JOIN r.cinema c " +
            "WHERE c.manager.id = :managerId " +
            "AND c.id = :cinemaId " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = com.org.Movie_Ticket_Booking.entity.enums.BookingStatus.PAID " +
            "GROUP BY " +
            "CASE " +
            "  WHEN HOUR(s.startTime) < 12 THEN 'Buổi sáng (6-12h)' " +
            "  WHEN HOUR(s.startTime) < 18 THEN 'Buổi chiều (12-18h)' " +
            "  ELSE 'Buổi tối (18-24h)' " +
            "END " +
            "ORDER BY COUNT(DISTINCT b) DESC")
    List<TimeSlotStatisticsResponse> getTimeSlotStatisticsByCinema(@Param("managerId") Long managerId,
                                                                   @Param("cinemaId") Long cinemaId,
                                                                   @Param("startDate") LocalDateTime startDate,
                                                                   @Param("endDate") LocalDateTime endDate);


    // --- By Manager ---
    @Query("SELECT new com.org.Movie_Ticket_Booking.dto.respone.RevenueStatisticsResponse(" +
            "CONCAT(YEAR(b.createdAt), '-', " +
            "CASE WHEN MONTH(b.createdAt) < 10 THEN CONCAT('0', MONTH(b.createdAt)) ELSE CAST(MONTH(b.createdAt) AS string) END, '-', " +
            "CASE WHEN DAY(b.createdAt) < 10 THEN CONCAT('0', DAY(b.createdAt)) ELSE CAST(DAY(b.createdAt) AS string) END), " +
            "SUM(bs.cost), " +
            "COUNT(DISTINCT b), " +
            "CONCAT(" +
            "CASE WHEN DAY(b.createdAt) < 10 THEN CONCAT('0', DAY(b.createdAt)) ELSE CAST(DAY(b.createdAt) AS string) END, '/', " +
            "CASE WHEN MONTH(b.createdAt) < 10 THEN CONCAT('0', MONTH(b.createdAt)) ELSE CAST(MONTH(b.createdAt) AS string) END, '/', " +
            "YEAR(b.createdAt))) " +
            "FROM Booking b " +
            "JOIN b.bookingSeats bs " +
            "JOIN b.showtime s " +
            "JOIN s.room r " +
            "JOIN r.cinema c " +
            "WHERE c.manager.id = :managerId " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = com.org.Movie_Ticket_Booking.entity.enums.BookingStatus.PAID " +
            "GROUP BY YEAR(b.createdAt), MONTH(b.createdAt), DAY(b.createdAt) " +
            "ORDER BY YEAR(b.createdAt) DESC, MONTH(b.createdAt) DESC, DAY(b.createdAt) DESC")
    List<RevenueStatisticsResponse> getDailyRevenueStatisticsByManager(@Param("managerId") Long managerId,
                                                                       @Param("startDate") LocalDateTime startDate,
                                                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.org.Movie_Ticket_Booking.dto.respone.RevenueStatisticsResponse(" +
            "CONCAT(YEAR(b.createdAt), '-', " +
            "CASE WHEN MONTH(b.createdAt) < 10 THEN CONCAT('0', MONTH(b.createdAt)) ELSE CAST(MONTH(b.createdAt) AS string) END), " +
            "SUM(bs.cost), " +
            "COUNT(DISTINCT b), " +
            "CONCAT(" +
            "CASE WHEN MONTH(b.createdAt) < 10 THEN CONCAT('0', MONTH(b.createdAt)) ELSE CAST(MONTH(b.createdAt) AS string) END, '/', " +
            "YEAR(b.createdAt))) " +
            "FROM Booking b " +
            "JOIN b.bookingSeats bs " +
            "JOIN b.showtime s " +
            "JOIN s.room r " +
            "JOIN r.cinema c " +
            "WHERE c.manager.id = :managerId " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = com.org.Movie_Ticket_Booking.entity.enums.BookingStatus.PAID " +
            "GROUP BY YEAR(b.createdAt), MONTH(b.createdAt) " +
            "ORDER BY YEAR(b.createdAt) DESC, MONTH(b.createdAt) DESC")
    List<RevenueStatisticsResponse> getMonthlyRevenueStatisticsByManager(@Param("managerId") Long managerId,
                                                                         @Param("startDate") LocalDateTime startDate,
                                                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.org.Movie_Ticket_Booking.dto.respone.RevenueStatisticsResponse(" +
            "CAST(YEAR(b.createdAt) AS string), SUM(bs.cost), COUNT(DISTINCT b), CAST(YEAR(b.createdAt) AS string)) " +
            "FROM Booking b " +
            "JOIN b.bookingSeats bs " +
            "JOIN b.showtime s " +
            "JOIN s.room r " +
            "JOIN r.cinema c " +
            "WHERE c.manager.id = :managerId " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = com.org.Movie_Ticket_Booking.entity.enums.BookingStatus.PAID " +
            "GROUP BY YEAR(b.createdAt) " +
            "ORDER BY YEAR(b.createdAt) DESC")
    List<RevenueStatisticsResponse> getYearlyRevenueStatisticsByManager(@Param("managerId") Long managerId,
                                                                        @Param("startDate") LocalDateTime startDate,
                                                                        @Param("endDate") LocalDateTime endDate);

    // --- By Cinema ---
    @Query("SELECT new com.org.Movie_Ticket_Booking.dto.respone.RevenueStatisticsResponse(" +
            "CONCAT(YEAR(b.createdAt), '-', " +
            "CASE WHEN MONTH(b.createdAt) < 10 THEN CONCAT('0', MONTH(b.createdAt)) ELSE CAST(MONTH(b.createdAt) AS string) END, '-', " +
            "CASE WHEN DAY(b.createdAt) < 10 THEN CONCAT('0', DAY(b.createdAt)) ELSE CAST(DAY(b.createdAt) AS string) END), " +
            "SUM(bs.cost), " +
            "COUNT(DISTINCT b), " +
            "CONCAT(" +
            "CASE WHEN DAY(b.createdAt) < 10 THEN CONCAT('0', DAY(b.createdAt)) ELSE CAST(DAY(b.createdAt) AS string) END, '/', " +
            "CASE WHEN MONTH(b.createdAt) < 10 THEN CONCAT('0', MONTH(b.createdAt)) ELSE CAST(MONTH(b.createdAt) AS string) END, '/', " +
            "YEAR(b.createdAt))) " +
            "FROM Booking b " +
            "JOIN b.bookingSeats bs " +
            "JOIN b.showtime s " +
            "JOIN s.room r " +
            "JOIN r.cinema c " +
            "WHERE c.manager.id = :managerId AND c.id = :cinemaId " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = com.org.Movie_Ticket_Booking.entity.enums.BookingStatus.PAID " +
            "GROUP BY YEAR(b.createdAt), MONTH(b.createdAt), DAY(b.createdAt) " +
            "ORDER BY YEAR(b.createdAt) DESC, MONTH(b.createdAt) DESC, DAY(b.createdAt) DESC")
    List<RevenueStatisticsResponse> getDailyRevenueStatisticsByCinema(@Param("managerId") Long managerId,
                                                                      @Param("cinemaId") Long cinemaId,
                                                                      @Param("startDate") LocalDateTime startDate,
                                                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.org.Movie_Ticket_Booking.dto.respone.RevenueStatisticsResponse(" +
            "CONCAT(YEAR(b.createdAt), '-', " +
            "CASE WHEN MONTH(b.createdAt) < 10 THEN CONCAT('0', MONTH(b.createdAt)) ELSE CAST(MONTH(b.createdAt) AS string) END), " +
            "SUM(bs.cost), " +
            "COUNT(DISTINCT b), " +
            "CONCAT(" +
            "CASE WHEN MONTH(b.createdAt) < 10 THEN CONCAT('0', MONTH(b.createdAt)) ELSE CAST(MONTH(b.createdAt) AS string) END, '/', " +
            "YEAR(b.createdAt))) " +
            "FROM Booking b " +
            "JOIN b.bookingSeats bs " +
            "JOIN b.showtime s " +
            "JOIN s.room r " +
            "JOIN r.cinema c " +
            "WHERE c.manager.id = :managerId AND c.id = :cinemaId " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = com.org.Movie_Ticket_Booking.entity.enums.BookingStatus.PAID " +
            "GROUP BY YEAR(b.createdAt), MONTH(b.createdAt) " +
            "ORDER BY YEAR(b.createdAt) DESC, MONTH(b.createdAt) DESC")
    List<RevenueStatisticsResponse> getMonthlyRevenueStatisticsByCinema(@Param("managerId") Long managerId,
                                                                        @Param("cinemaId") Long cinemaId,
                                                                        @Param("startDate") LocalDateTime startDate,
                                                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.org.Movie_Ticket_Booking.dto.respone.RevenueStatisticsResponse(" +
            "CAST(YEAR(b.createdAt) AS string), SUM(bs.cost), COUNT(DISTINCT b), CAST(YEAR(b.createdAt) AS string)) " +
            "FROM Booking b " +
            "JOIN b.bookingSeats bs " +
            "JOIN b.showtime s " +
            "JOIN s.room r " +
            "JOIN r.cinema c " +
            "WHERE c.manager.id = :managerId AND c.id = :cinemaId " +
            "AND b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status = com.org.Movie_Ticket_Booking.entity.enums.BookingStatus.PAID " +
            "GROUP BY YEAR(b.createdAt) " +
            "ORDER BY YEAR(b.createdAt) DESC")
    List<RevenueStatisticsResponse> getYearlyRevenueStatisticsByCinema(@Param("managerId") Long managerId,
                                                                       @Param("cinemaId") Long cinemaId,
                                                                       @Param("startDate") LocalDateTime startDate,
                                                                       @Param("endDate") LocalDateTime endDate);
}
