package com.org.Movie_Ticket_Booking.controller.admin;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController extends AdminController{
    @GetMapping("/account")
    public String Users(Model model){
        model.addAttribute("activePage", "account");
        model.addAttribute("content", ViewNames.CONTENT_ACCOUNT);
        return ViewNames.LAYOUT_ADMIN;
    }
}
