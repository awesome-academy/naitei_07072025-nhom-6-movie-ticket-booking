package com.org.Movie_Ticket_Booking.controller.admin;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController extends AdminController {
    @GetMapping("/statistics")
    public String statistics(Model model){
        model.addAttribute("activePage", "statistics");
        model.addAttribute("content", ViewNames.CONTENT_STATISTICS);
        return ViewNames.LAYOUT_ADMIN;
    }
}
