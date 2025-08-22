package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.Movie;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {


    // Lấy danh sách phim theo rạp - phân trang
    @Query("SELECT m FROM Movie m JOIN m.cinemas c WHERE c.id = :cinemaId")
    Page<Movie> findAllByCinemaId(@Param("cinemaId") Long cinemaId, Pageable pageable);

    // Lấy danh sách phim theo rạp - không phân trang
    @Query("SELECT m FROM Movie m JOIN m.cinemas c WHERE c.id = :cinemaId")
    List<Movie> findAllByCinemaId(@Param("cinemaId") Long cinemaId);

    // Lấy phim chưa có trong rạp - phân trang
    @Query("SELECT m FROM Movie m WHERE m.id NOT IN (SELECT m2.id FROM Movie m2 JOIN m2.cinemas c WHERE c.id = :cinemaId)")
    Page<Movie> findAllNotInCinema(@Param("cinemaId") Long cinemaId, Pageable pageable);

    @Query("SELECT EXISTS (SELECT 1 FROM Movie m JOIN m.cinemas c WHERE c.id = :cinemaId AND m.id = :movieId)")
    boolean existsInCinema(@Param("cinemaId") Long cinemaId, @Param("movieId") Long movieId);

    Optional<Movie> findByTitleAndReleaseDate(@NotBlank(message = "Tiêu đề phim không được để trống") String title, LocalDate releaseDate);
}
