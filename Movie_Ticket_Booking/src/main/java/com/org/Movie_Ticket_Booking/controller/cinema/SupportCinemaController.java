package com.org.Movie_Ticket_Booking.controller.cinema;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupportCinemaController extends CinemaController {

    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("activePage", "support");
        model.addAttribute("content", ViewNames.CINEMA_SUPPORT);
        return ViewNames.LAYOUT_CINEMA;
    }
}
