package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface CinemaRepository extends ListCrudRepository<Cinema, Long> {

    public Optional<Cinema> findByAddress(String address);
}
