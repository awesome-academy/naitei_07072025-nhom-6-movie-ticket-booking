package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Movie;
import com.org.Movie_Ticket_Booking.service.CinemaMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/manager/movies")
@RequiredArgsConstructor
public class MoviesController {

    private final CinemaMovieService cinemaMovieService;

    @GetMapping
    public String listMovies(@RequestParam(required = false) Long cinemaId,
                             @RequestParam(defaultValue = "all") String filter,
                             @PageableDefault(size = 8) Pageable pageable,
                             Model model) {

        List<Cinema> cinemas = cinemaMovieService.getCinemasOfManager();
        Page<Movie> moviesPage = cinemaMovieService.getMoviesForCinema(cinemaId, filter, pageable);
        Set<Long> selectedMovieIds = cinemaMovieService.getSelectedMovieIds(cinemaId);

        model.addAttribute("cinemas", cinemas);
        model.addAttribute("movies", moviesPage);
        model.addAttribute("selectedMovieIds", selectedMovieIds);
        model.addAttribute("cinemaId", cinemaId);
        model.addAttribute("filter", filter);

        // Các thuộc tính để layout hoạt động đúng
        model.addAttribute("activePage", "movies");
        model.addAttribute("content", "CinemaManager/movies :: content");
        model.addAttribute("styles", "CinemaManager/movies :: styles");

        // Luôn trả về file layout chính
        return "CinemaManager/layoutManager";
    }

    @PostMapping("/add")
    public String addMovie(@RequestParam Long cinemaId,
                           @RequestParam Long movieId) {
        cinemaMovieService.addMovieToCinema(cinemaId, movieId);
        return "redirect:/manager/movies?cinemaId=" + cinemaId;
    }
}
