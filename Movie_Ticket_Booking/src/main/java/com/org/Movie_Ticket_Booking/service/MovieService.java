package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    public List<Movie> findAll();
    
    public Optional<Movie> findById(Long id);

}
