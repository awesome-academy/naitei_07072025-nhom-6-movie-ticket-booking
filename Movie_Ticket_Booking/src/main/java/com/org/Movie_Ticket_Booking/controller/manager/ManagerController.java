package com.org.Movie_Ticket_Booking.controller.manager;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
@PreAuthorize("hasRole('CINEMA_MANAGER')")
public abstract class ManagerController {
}
