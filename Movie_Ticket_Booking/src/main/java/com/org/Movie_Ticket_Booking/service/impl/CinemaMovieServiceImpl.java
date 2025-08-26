package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Movie;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.CinemaRepository;
import com.org.Movie_Ticket_Booking.repository.MovieRepository;
import com.org.Movie_Ticket_Booking.service.CinemaMovieService;
import com.org.Movie_Ticket_Booking.service.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CinemaMovieServiceImpl implements CinemaMovieService {

    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final UserHelper userHelper;

    @Override
    public List<Cinema> getCinemasOfManager() {
        User user = userHelper.getCurrentUser();
        return cinemaRepository.findByManagerId(user.getId());
    }

    @Override
    public Page<Movie> getMoviesForCinema(Long cinemaId, String filter, Pageable pageable) {
        if (cinemaId == null) {
            return Page.empty(pageable);
        }

        if (filter == null || filter.trim().isEmpty()) {
            return movieRepository.findAll(pageable);
        }

        return switch (filter.toLowerCase()) {
            case "selected" -> movieRepository.findByCinemas_Id(cinemaId, pageable);
            case "unselected" -> movieRepository.findAllNotInCinema(cinemaId, pageable);
            default -> movieRepository.findAll(pageable);
        };
    }

    /** Tập id phim đã thuộc rạp */
    @Override
    public Set<Long> getSelectedMovieIds(Long cinemaId) {
        if (cinemaId == null) return Set.of();
        return movieRepository.findIdsByCinemas_Id(cinemaId);
    }

    /** Thêm phim vào rạp */
    @Override
    @Transactional
    public void addMovieToCinema(Long cinemaId, Long movieId) {
        if (cinemaId == null) {
            throw new AppException(ErrorCode.CINEMA_ID_REQUIRED);
        }
        if (movieId == null) {
            throw new AppException(ErrorCode.MOVIE_ID_REQUIRED);
        }

        if (movieRepository.existsByCinemas_IdAndId(cinemaId, movieId)) {
            throw new AppException(ErrorCode.MOVIE_ALREADY_EXISTS_IN_CINEMA);
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
