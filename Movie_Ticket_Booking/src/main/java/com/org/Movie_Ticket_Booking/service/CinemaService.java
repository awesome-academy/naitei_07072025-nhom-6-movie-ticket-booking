package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.entity.Cinema;

import java.util.List;

public interface CinemaService {

    public List<Cinema> getCinamaByAddress(String address);

    List<Cinema> findByManagerId(Long id);
}
