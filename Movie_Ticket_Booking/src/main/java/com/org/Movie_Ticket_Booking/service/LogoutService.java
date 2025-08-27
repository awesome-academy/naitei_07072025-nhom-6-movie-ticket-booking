package com.org.Movie_Ticket_Booking.service;

import org.springframework.transaction.annotation.Transactional;

public interface LogoutService {
    @Transactional
    public void logout(String token);

}
