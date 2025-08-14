package com.org.Movie_Ticket_Booking.entity;

import com.org.Movie_Ticket_Booking.entity.enums.PromotionUserStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "promotion_users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ tới Promotion
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    // Quan hệ tới User
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private PromotionUserStatus status;
}

