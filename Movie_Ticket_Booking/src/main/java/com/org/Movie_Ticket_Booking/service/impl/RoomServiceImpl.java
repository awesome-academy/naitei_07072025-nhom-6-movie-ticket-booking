package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.entity.Room;
import com.org.Movie_Ticket_Booking.repository.RoomRepository;
import com.org.Movie_Ticket_Booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository){
        this.roomRepository=roomRepository;
    }

    @Override
    public List<Room> findRoomByCinemaId(Long id) {
        return this.roomRepository.findByCinemaId(id);
    }

    @Override
    public List<Room> findAll() {
        return this.roomRepository.findAll();
    }

}
