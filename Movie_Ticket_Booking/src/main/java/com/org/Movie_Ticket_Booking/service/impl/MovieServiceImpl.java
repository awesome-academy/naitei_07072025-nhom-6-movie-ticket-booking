package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.Movie;
import com.org.Movie_Ticket_Booking.repository.MovieRepository;
import com.org.Movie_Ticket_Booking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository){
        this.movieRepository=movieRepository;
    }

    @Override
    public List<Movie> findAll() {
        return this.movieRepository.findAll();
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return this.movieRepository.findById(id);
    }
}
