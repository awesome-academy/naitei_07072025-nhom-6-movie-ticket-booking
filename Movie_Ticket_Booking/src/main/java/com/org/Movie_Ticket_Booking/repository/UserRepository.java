package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);
}
