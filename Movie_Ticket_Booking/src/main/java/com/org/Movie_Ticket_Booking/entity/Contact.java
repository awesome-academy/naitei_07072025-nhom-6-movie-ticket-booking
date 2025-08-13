package com.org.Movie_Ticket_Booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ tới User
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 255)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

