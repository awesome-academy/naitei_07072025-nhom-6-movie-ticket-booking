package com.org.Movie_Ticket_Booking.controller.cinema;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoviesController extends CinemaController {

    // Danh sách phim admin đã thêm
    @GetMapping("/movies")
    public String listMovies(Model model) {
        model.addAttribute("activePage", "movies");
        model.addAttribute("content", ViewNames.CINEMA_MOVIES);
        return ViewNames.LAYOUT_CINEMA;
    }
}
