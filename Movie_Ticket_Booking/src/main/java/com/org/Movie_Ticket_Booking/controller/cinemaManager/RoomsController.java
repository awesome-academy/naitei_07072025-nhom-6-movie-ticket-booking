package com.org.Movie_Ticket_Booking.controller.cinemaManager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomsController extends CinemaController {

    // Quản lý rạp chiếu
    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("activePage", "rooms");
        model.addAttribute("content", ViewNames.CINEMA_ROOMS);
        return ViewNames.LAYOUT_CINEMA;
    }
}