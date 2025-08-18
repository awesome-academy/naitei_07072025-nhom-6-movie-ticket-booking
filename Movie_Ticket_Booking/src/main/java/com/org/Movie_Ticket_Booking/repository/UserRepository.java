package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByNumberPhone(String numberPhone);

    @Query("SELECT u FROM User u WHERE u.email = :username OR u.numberPhone = :username")
    Optional<User> findByEmailOrPhone(@Param("username") String username);
}
