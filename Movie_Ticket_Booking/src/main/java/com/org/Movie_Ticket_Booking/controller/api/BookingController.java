package com.org.Movie_Ticket_Booking.controller.api;

import com.org.Movie_Ticket_Booking.dto.request.BookingRequest;
import com.org.Movie_Ticket_Booking.dto.respone.BookingResponse;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService=bookingService;
    }

    @PostMapping("/ticket")
    public BookingResponse bookTicket(@RequestBody BookingRequest bookingRequest, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return this.bookingService.bookTicket(bookingRequest, user);
    }
}
