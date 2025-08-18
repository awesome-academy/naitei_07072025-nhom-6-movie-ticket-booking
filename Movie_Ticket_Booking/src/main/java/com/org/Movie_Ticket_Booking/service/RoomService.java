package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.entity.Room;

import java.util.List;

public interface RoomService {

    List<Room> findRoomByCinemaId(Long id);

    List<Room> findAll();
}
