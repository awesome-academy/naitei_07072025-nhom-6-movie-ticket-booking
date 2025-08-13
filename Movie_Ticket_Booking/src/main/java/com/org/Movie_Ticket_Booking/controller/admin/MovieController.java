package com.org.Movie_Ticket_Booking.controller.admin;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MovieController extends AdminController{
    @GetMapping("/movies")
    public String movies(Model model){
        model.addAttribute("activePage", "movies");
        model.addAttribute("content", ViewNames.CONTENT_MOVIES);
        return ViewNames.LAYOUT_ADMIN;
    }
}
