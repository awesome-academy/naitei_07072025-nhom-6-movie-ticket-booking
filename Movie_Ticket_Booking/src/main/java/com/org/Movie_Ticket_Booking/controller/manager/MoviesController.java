package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoviesController extends ManagerController {

    // Danh sách phim admin đã thêm
    @GetMapping("/movies")
    public String listMovies(Model model) {
        model.addAttribute("activePage", "movies");
        model.addAttribute("content", ViewNames.MANAGER_MOVIES);
        return ViewNames.LAYOUT_MANAGER;
    }
}
