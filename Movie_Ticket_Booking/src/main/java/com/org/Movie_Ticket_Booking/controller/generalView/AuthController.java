package com.org.Movie_Ticket_Booking.controller.generalView;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("adminManagerAuthController")
public class AuthController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String redirectByRole(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login?error";
        }

        var authorities = authentication.getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/login?error";
    }
}
