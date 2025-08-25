package com.org.Movie_Ticket_Booking.controller.generalView;

import com.org.Movie_Ticket_Booking.constants.RoleConstants;
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

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals(RoleConstants.ROLE_ADMIN))) {
            return "redirect:/admin/dashboard";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals(RoleConstants.ROLE_CINEMA_MANAGER))) {
            return "redirect:/manager/dashboard";
        }

        return "redirect:/login?error";
    }
}
