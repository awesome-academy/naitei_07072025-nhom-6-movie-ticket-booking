package com.org.Movie_Ticket_Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "genres")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên thể loại không được để trống")
    @Column(nullable = false, length = 100)
    private String name;

    // Many-to-Many với Movie
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Movie> movies;
}

