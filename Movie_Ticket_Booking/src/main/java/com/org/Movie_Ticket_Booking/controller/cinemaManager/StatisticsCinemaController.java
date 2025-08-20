package com.org.Movie_Ticket_Booking.controller.cinemaManager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsCinemaController extends CinemaController {

    @GetMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("activePage", "statistics");
        model.addAttribute("content", ViewNames.CINEMA_STATISTICS);
        return ViewNames.LAYOUT_CINEMA;
    }
}