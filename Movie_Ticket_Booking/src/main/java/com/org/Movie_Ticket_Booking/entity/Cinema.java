package com.org.Movie_Ticket_Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "cinemas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên rạp không được để trống")
    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String address;

    @Column(name = "map_url", columnDefinition = "TEXT")
    private String mapUrl;

    // Quan hệ tới Cinema Manger
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = false)
    @JsonIgnore
    private User manager;

    // Many-to-Many với Movie
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cinema_movies",
            joinColumns = @JoinColumn(name = "cinema_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private Set<Movie> movies;

    // Liên kết tới Room
    @OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Room> rooms;

}

