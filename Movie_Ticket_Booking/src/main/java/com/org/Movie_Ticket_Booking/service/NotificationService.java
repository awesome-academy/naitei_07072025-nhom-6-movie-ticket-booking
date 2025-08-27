package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.entity.User;

import java.util.Locale;

public interface NotificationService {
    void saveNoti(User user, String code, Object[] args, String method, Locale locale);
}
