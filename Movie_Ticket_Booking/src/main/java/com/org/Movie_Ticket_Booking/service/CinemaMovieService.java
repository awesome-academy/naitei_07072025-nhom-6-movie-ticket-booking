package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.entity.Movie;
import java.util.List;
import java.util.Set;

public interface CinemaMovieService {
    Long getCurrentManagerCinemaId();
    List<Movie> getMoviesForCinema(Long cinemaId, String filter);
    Set<Long> getSelectedMovieIds(Long cinemaId);
    void addMovieToCinema(Long movieId);
}