package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Movie;
import com.org.Movie_Ticket_Booking.service.CinemaMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
//@RequestMapping("/movies")
@RequiredArgsConstructor
public class MoviesController extends ManagerController {

    private final CinemaMovieService cinemaMovieService;

    // Hiển thị danh sách phim theo rạp + filter + phân trang
    @GetMapping("/movies/{cinemaId}")
    public String listMovies(@PathVariable Long cinemaId,
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

        // Thuộc tính chung cho layout
        model.addAttribute("activePage", "movies");
        model.addAttribute("content", ViewNames.MANAGER_MOVIES);

        return ViewNames.LAYOUT_MANAGER;
    }

    @GetMapping("/movies")
    public String listMoviesDefault(@RequestParam(defaultValue = "all") String filter,
                                    @PageableDefault(size = 8) Pageable pageable,
                                    Model model) {

        List<Cinema> cinemas = cinemaMovieService.getCinemasOfManager();

        // Nếu manager có ít nhất 1 cinema, redirect đến cinema đầu tiên
        if (!cinemas.isEmpty()) {
            return "redirect:/manager/movies/" + cinemas.get(0).getId();
        }

        // Nếu không có cinema nào, hiển thị trang trống
        model.addAttribute("cinemas", cinemas);
        model.addAttribute("movies", Page.empty());
        model.addAttribute("selectedMovieIds", Set.of());
        model.addAttribute("activePage", "movies");
        model.addAttribute("content", ViewNames.MANAGER_MOVIES);

        return ViewNames.LAYOUT_MANAGER;
    }


    // Thêm phim vào rạp
    @PostMapping("/movies/add")
    public String addMovie(@RequestParam Long cinemaId,
                           @RequestParam Long movieId) {
        cinemaMovieService.addMovieToCinema(cinemaId, movieId);

        return "redirect:/manager/movies/" + cinemaId;
    }
}
