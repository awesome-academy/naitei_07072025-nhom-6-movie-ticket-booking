package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ManagerAuthController extends ManagerController {
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("content", ViewNames.MANAGER_DASHBOARD);
        return ViewNames.LAYOUT_MANAGER;
    }
}
