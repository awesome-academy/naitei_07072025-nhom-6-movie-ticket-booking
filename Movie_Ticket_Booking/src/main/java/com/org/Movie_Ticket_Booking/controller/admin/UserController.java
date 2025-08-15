package com.org.Movie_Ticket_Booking.controller.admin;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController extends AdminController{
    @GetMapping("/users")
    public String Users(Model model){
        model.addAttribute("activePage", "users");
        model.addAttribute("content", ViewNames.CONTENT_USERS);
        return ViewNames.LAYOUT_ADMIN;
    }
}
