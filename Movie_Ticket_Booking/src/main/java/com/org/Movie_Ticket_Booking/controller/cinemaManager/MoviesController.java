package com.org.Movie_Ticket_Booking.controller.cinemaManager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import com.org.Movie_Ticket_Booking.entity.Movie;
import com.org.Movie_Ticket_Booking.service.CinemaMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MoviesController extends CinemaController {

    private final CinemaMovieService cinemaMovieService;

    // Danh sách phim admin đã thêm - kho chứa phim
    @GetMapping("/movies")
    public String listMovies(@RequestParam(value = "filter", defaultValue = "all") String filter,
                             Model model) {

        Long cinemaId = cinemaMovieService.getCurrentManagerCinemaId();
        List<Movie> movies = cinemaMovieService.getMoviesForCinema(cinemaId, filter);
        Set<Long> selectedMovieIds = cinemaMovieService.getSelectedMovieIds(cinemaId);

        model.addAttribute("activePage", "movies");
        model.addAttribute("filter", filter);
        model.addAttribute("movies", movies);
        model.addAttribute("selectedMovieIds", selectedMovieIds);

        // fragment sẽ được nhúng vào layoutCinema.html
        model.addAttribute("content", ViewNames.CINEMA_MOVIES);
        return ViewNames.LAYOUT_CINEMA;
    }

    // Thêm phim vào rạp của manager hiện tại
    @PostMapping("/movies/{movieId}/add")
    public String addMovie(@PathVariable Long movieId,
                           @RequestParam(value = "filter", defaultValue = "all") String filter) {
        cinemaMovieService.addMovieToCinema(movieId);
        // trở lại trang đang xem (giữ nguyên filter)
        return "redirect:/manager/movies?filter=" + filter;
    }
}