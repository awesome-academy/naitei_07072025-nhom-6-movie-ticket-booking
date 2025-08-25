package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.UpgradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UpgradeRequestRepository extends JpaRepository<UpgradeRequest, Long> {
    Optional<UpgradeRequest> findByUser_IdAndCinemaNameAndAddress(Long userID, String name, String address);
    Optional<UpgradeRequest> findByCinemaNameAndAddress(String name, String address);
}
