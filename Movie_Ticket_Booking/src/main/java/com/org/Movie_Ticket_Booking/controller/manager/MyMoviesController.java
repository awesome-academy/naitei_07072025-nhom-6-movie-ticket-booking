package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyMoviesController extends ManagerController {

    // Phim của rạp (quản lý suất chiếu, phòng chiếu)
    @GetMapping("/my-movies")
    public String myMovies(Model model) {
        model.addAttribute("activePage", "my_movies");
        model.addAttribute("content", ViewNames.MANAGER_MY_MOVIES);
        return ViewNames.LAYOUT_MANAGER;
    }
}
