package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.Movie;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Lấy danh sách phim theo rạp - phân trang
    Page<Movie> findByCinemas_Id(Long cinemaId, Pageable pageable);

    // Lấy phim chưa có trong rạp - phân trang
    @Query("SELECT m FROM Movie m WHERE m.id NOT IN (SELECT m2.id FROM Movie m2 JOIN m2.cinemas c WHERE c.id = :cinemaId)")
    Page<Movie> findAllNotInCinema(@Param("cinemaId") Long cinemaId, Pageable pageable);

    // Kiểm tra movie có tồn tại trong cinema không
    boolean existsByCinemas_IdAndId(Long cinemaId, Long movieId);

    // Tìm movie theo title và releaseDate
    Optional<Movie> findByTitleAndReleaseDate(@NotBlank(message = "Tiêu đề phim không được để trống") String title, LocalDate releaseDate);

    // Lấy ID của movies theo cinema
    Set<Long> findIdsByCinemas_Id(Long cinemaId);

}
