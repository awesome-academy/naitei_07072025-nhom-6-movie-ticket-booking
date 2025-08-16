package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.dto.request.UserRegister;
import com.org.Movie_Ticket_Booking.dto.respone.RegisterRespone;

public interface AuthService {
    RegisterRespone register(UserRegister request);
}
