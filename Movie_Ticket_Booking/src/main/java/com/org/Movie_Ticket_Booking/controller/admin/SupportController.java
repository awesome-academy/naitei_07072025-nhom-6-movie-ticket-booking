package com.org.Movie_Ticket_Booking.controller.admin;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SupportController extends AdminController{
    @GetMapping("/support")
    public String support(Model model){
        model.addAttribute("activePage", "support");
        model.addAttribute("content", ViewNames.CONTENT_SUPPORT);
        return ViewNames.LAYOUT_ADMIN;
    }
}
