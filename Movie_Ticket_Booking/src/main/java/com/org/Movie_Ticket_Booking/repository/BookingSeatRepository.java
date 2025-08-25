package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.BookingSeat;
import com.org.Movie_Ticket_Booking.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    Set<BookingSeat> findBySeatIdInAndBooking_Showtime(Set<Long> seatIds, Showtime showtime);

}
