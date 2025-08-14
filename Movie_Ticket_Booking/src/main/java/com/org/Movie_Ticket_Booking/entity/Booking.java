package com.org.Movie_Ticket_Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.org.Movie_Ticket_Booking.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ tới User
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Quan hệ tới Showtime
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "showtimes_id", nullable = false)
    private Showtime showtime;

    // Quan hệ tới Promotion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Liên kết tới BookingSeat
    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<BookingSeat> bookingSeats;

    // Liên kết tới Payment
    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Payment> payments;
}

