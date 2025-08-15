package com.org.Movie_Ticket_Booking.controller.admin;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PromotionController extends AdminController{
    @GetMapping("/promotions")
    public String promotion(Model model){
        model.addAttribute("activePage", "promotion");
        model.addAttribute("content", ViewNames.CONTENT_PROMOTIONS);
        return ViewNames.LAYOUT_ADMIN;
    }
}
