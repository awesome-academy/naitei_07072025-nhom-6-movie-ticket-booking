package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long id);
}
