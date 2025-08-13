package com.org.Movie_Ticket_Booking.controller.admin;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RequestCinema extends AdminController{
    @GetMapping("/request_cinemas")
    public String requestCinemas(Model model){
        model.addAttribute("activePage", "request");
        model.addAttribute("content", ViewNames.CONTENT_REQUEST_CINEMAS);
        return ViewNames.LAYOUT_ADMIN;
    }
}
