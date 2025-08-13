package com.org.Movie_Ticket_Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "movies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề phim không được để trống")
    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Min(value = 1, message = "Thời lượng phải lớn hơn 0")
    private Integer duration;

    @Column(name = "poster_url", columnDefinition = "TEXT")
    private String posterUrl;

    @Size(max = 50, message = "Ngôn ngữ tối đa 50 ký tự")
    @Column(length = 50)
    private String language;

    // Many-to-Many với Genre
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genres_id")
    )
    private Set<Genre> genres;

    // Many-to-Many với Cinema
    @ManyToMany(mappedBy = "movies", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Cinema> cinemas;

    // Liên kết với Showtime
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Showtime> showtimes;

    // Liên kết với Review
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Review> reviews;
}

