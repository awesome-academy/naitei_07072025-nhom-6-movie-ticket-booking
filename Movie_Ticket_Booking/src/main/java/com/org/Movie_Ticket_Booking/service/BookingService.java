package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.dto.request.BookingRequest;
import com.org.Movie_Ticket_Booking.dto.respone.BookingResponse;
import com.org.Movie_Ticket_Booking.entity.User;

public interface BookingService {

    public BookingResponse bookTicket(BookingRequest bookingRequest, User user);
}
