package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.Room;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface RoomRepository extends ListCrudRepository<Room, Long> {

    List<Room> findByCinemaId(Long cinemaId);

}
