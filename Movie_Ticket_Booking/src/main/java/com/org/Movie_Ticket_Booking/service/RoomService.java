package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<Room> findRoomByCinemaId(Long id);

    List<Room> findAll();

    Optional<Room> findById(Long id);
}
