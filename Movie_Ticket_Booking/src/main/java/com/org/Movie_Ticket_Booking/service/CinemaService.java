package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.entity.Cinema;

import java.util.List;
import java.util.Optional;

public interface CinemaService {

    public List<Cinema> getCinamaByAddress(String address);

    public List<Cinema> findAll();

    public Optional<Cinema> findById(Long id);
}
