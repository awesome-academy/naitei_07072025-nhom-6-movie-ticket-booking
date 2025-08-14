package com.org.Movie_Ticket_Booking.entity;

import com.org.Movie_Ticket_Booking.entity.enums.UpgradeRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "upgrade_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpgradeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ tới User (người gửi yêu cầu)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "cinema_name", length = 100)
    private String cinemaName;

    @Column(length = 255)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private UpgradeRequestStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

