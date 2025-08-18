package com.org.Movie_Ticket_Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "seats")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Số ghế không được để trống")
    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    // Quan hệ tới Room
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // Quan hệ tới TypeSeat
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_type_id", nullable = false)
    @JsonIgnore
    private TypeSeat typeSeat;

    // Quan hệ tới BookingSeat
    @OneToMany(mappedBy = "seat", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<BookingSeat> bookingSeats;
}

