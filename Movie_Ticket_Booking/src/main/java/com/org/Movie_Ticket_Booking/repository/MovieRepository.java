package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Phim đã thuộc rạp
    @Query("select m from Cinema c join c.movies m where c.id = :cinemaId")
    List<Movie> findAllByCinemaId(Long cinemaId);

    // Phim chưa thuộc rạp
    @Query("""
           select m from Movie m
           where m.id not in (
               select m2.id from Cinema c join c.movies m2 where c.id = :cinemaId
           )
           """)
    List<Movie> findAllNotInCinema(Long cinemaId);

    // Check phim đã thuộc rạp chưa?
    @Query("select (count(m) > 0) from Cinema c join c.movies m where c.id = :cinemaId and m.id = :movieId")
    boolean existsInCinema(Long cinemaId, Long movieId);
}
