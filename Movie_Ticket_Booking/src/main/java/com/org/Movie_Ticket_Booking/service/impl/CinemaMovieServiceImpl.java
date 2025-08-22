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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaMovieServiceImpl implements CinemaMovieService {

    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public List<Cinema> getCinemasOfManager() {
        User user = getCurrentUser();
        return cinemaRepository.findAll().stream()
                .filter(c -> Objects.equals(c.getManager().getId(), user.getId()))
                .toList();
    }



    @Override
    public Page<Movie> getMoviesForCinema(Long cinemaId, String filter, Pageable pageable) {
        if (cinemaId == null) {
            return Page.empty(pageable);
        }

        return switch (filter.toLowerCase()) {
            case "selected" -> movieRepository.findAllByCinemaId(cinemaId, pageable);
            case "unselected" -> movieRepository.findAllNotInCinema(cinemaId, pageable);
            default -> movieRepository.findAll(pageable);
        };
    }

    /** Tập id phim đã thuộc rạp */
    @Override
    public Set<Long> getSelectedMovieIds(Long cinemaId) {
        if (cinemaId == null) return Set.of();
        return movieRepository.findAllByCinemaId(cinemaId)
                .stream().map(Movie::getId).collect(Collectors.toSet());
    }

    /** Thêm phim vào rạp */
    @Override
    public void addMovieToCinema(Long cinemaId, Long movieId) {
        if (cinemaId == null) throw new AppException(ErrorCode.INVALID_KEY);

        if (movieRepository.existsInCinema(cinemaId, movieId)) {
            return;
        }

        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        if (cinema.getMovies() == null) {
            cinema.setMovies(new HashSet<>());
        }
        cinema.getMovies().add(movie);
        cinemaRepository.save(cinema);
    }
}
