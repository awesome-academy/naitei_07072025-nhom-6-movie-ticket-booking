package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.dto.request.BookingRequest;
import com.org.Movie_Ticket_Booking.dto.respone.BookingHistory;
import com.org.Movie_Ticket_Booking.dto.respone.BookingResponse;
import com.org.Movie_Ticket_Booking.entity.User;

import java.util.List;

public interface BookingService {

    public BookingResponse bookTicket(BookingRequest bookingRequest, User user);

    public List<BookingHistory> findBookingByUserId(Long id);
}
