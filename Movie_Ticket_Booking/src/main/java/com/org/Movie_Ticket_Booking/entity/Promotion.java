package com.org.Movie_Ticket_Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "promo_code", length = 50)
    private String promoCode;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "limited_quantity")
    private Integer limitedQuantity;

    @Column(precision = 5, scale = 2)
    private BigDecimal discount;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PromotionUser> promotionUsers;
}

