package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.dto.request.LoginRequest;
import com.org.Movie_Ticket_Booking.dto.respone.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest request);
}
