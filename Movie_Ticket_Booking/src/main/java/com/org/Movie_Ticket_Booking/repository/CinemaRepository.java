package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CinemaRepository extends ListCrudRepository<Cinema, Long> {

    public Optional<Cinema> findByAddress(String address);

    @Query("SELECT c FROM Cinema c WHERE c.manager.id = :managerId")
    List<Cinema> findByManagerId(@Param("managerId") Long managerId);
}
