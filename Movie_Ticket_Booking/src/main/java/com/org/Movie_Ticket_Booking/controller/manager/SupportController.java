package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller("managerSupportController")
public class SupportController extends ManagerController {

    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("activePage", "support");
        model.addAttribute("content", ViewNames.MANAGER_SUPPORT);
        return ViewNames.LAYOUT_MANAGER;
    }
}
