package com.org.Movie_Ticket_Booking.controller.generalView;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
