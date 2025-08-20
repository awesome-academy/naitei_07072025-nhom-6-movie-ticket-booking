package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.repository.CinemaRepository;
import com.org.Movie_Ticket_Booking.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CinemaServiceImpl implements CinemaService {

    private CinemaRepository cinemaRepository;

    @Autowired
    public CinemaServiceImpl(CinemaRepository cinemaRepository){
        this.cinemaRepository=cinemaRepository;
    }

    @Override
    public List<Cinema> getCinamaByAddress(String address) {
        return this.cinemaRepository.findByAddress(address).stream().toList();
    }

    @Override
    public List<Cinema> findAll(){
        return this.cinemaRepository.findAll();
    }

    @Override
    public Optional<Cinema> findById(Long id) {
        return this.cinemaRepository.findById(id);
    }
}
