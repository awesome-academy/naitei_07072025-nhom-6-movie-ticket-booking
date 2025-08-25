package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller("managerStatisticsController")
public class StatisticsController extends ManagerController {

    @GetMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("activePage", "statistics");
        model.addAttribute("content", ViewNames.MANAGER_STATISTICS);
        return ViewNames.LAYOUT_MANAGER;
    }
}
