package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CinemaMovieService {

    //Lấy danh sách rạp mà manager hiện tại quản lý
    List<Cinema> getCinemasOfManager();

    // Lấy danh sách phim theo filter: all | selected | unselected
    Page<Movie> getMoviesForCinema(Long cinemaId, String filter, Pageable pageable);

    // Lấy tập ID các phim đã thuộc rạp
    Set<Long> getSelectedMovieIds(Long cinemaId);

    // Thêm 1 phim vào rạp
    void addMovieToCinema(Long cinemaId, Long movieId);
}
