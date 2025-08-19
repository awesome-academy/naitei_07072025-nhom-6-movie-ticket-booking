package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.Showtime;
import com.org.Movie_Ticket_Booking.repository.ShowtimeRepository;
import com.org.Movie_Ticket_Booking.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {

    private ShowtimeRepository showtimeRepository;

    @Autowired
    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository){
        this.showtimeRepository=showtimeRepository;
    }

    @Override
    public Showtime addShowtime(Showtime s) {
        return this.showtimeRepository.save(s);
    }
}
