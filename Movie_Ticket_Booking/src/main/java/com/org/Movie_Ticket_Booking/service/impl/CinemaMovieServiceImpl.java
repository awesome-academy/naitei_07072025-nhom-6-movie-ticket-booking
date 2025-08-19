package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Movie;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.CinemaRepository;
import com.org.Movie_Ticket_Booking.repository.MovieRepository;
import com.org.Movie_Ticket_Booking.repository.UserRepository;
import com.org.Movie_Ticket_Booking.service.CinemaMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaMovieServiceImpl implements CinemaMovieService {

    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final UserRepository userRepository;

    // Lấy cinemaId từ user đang đăng nhập (email)
    @Override
    public Long getCurrentManagerCinemaId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Cinema cinema = cinemaRepository.findByManagerId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_FOUND));

        return cinema.getId();
    }

    // Danh sách phim theo filter: all/selected/unselected
    @Override
    public List<Movie> getMoviesForCinema(Long cinemaId, String filter) {
        if ("selected".equalsIgnoreCase(filter)) {
            return movieRepository.findAllByCinemaId(cinemaId);
        }
        if ("unselected".equalsIgnoreCase(filter)) {
            return movieRepository.findAllNotInCinema(cinemaId);
        }
        return movieRepository.findAll();
    }

    // Tập id phim đã thuộc rạp -> để render badge “Đã thêm”
    @Override
    public Set<Long> getSelectedMovieIds(Long cinemaId) {
        return movieRepository.findAllByCinemaId(cinemaId)
                .stream().map(Movie::getId).collect(Collectors.toSet());
    }

    // Thêm 1 phim vào rạp (id rạp lấy từ manager hiện tại)
    @Override
    public void addMovieToCinema(Long movieId) {
        Long cinemaId = getCurrentManagerCinemaId();

        if (movieRepository.existsInCinema(cinemaId, movieId)) {
            return;
        }

        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_FOUND));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        if (cinema.getMovies() == null) {
            cinema.setMovies(new HashSet<>());
        }
        cinema.getMovies().add(movie);

        cinemaRepository.save(cinema);
    }
}