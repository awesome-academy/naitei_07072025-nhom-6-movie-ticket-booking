package com.org.Movie_Ticket_Booking.repository;

import com.org.Movie_Ticket_Booking.dto.respone.ReviewRespone;
import com.org.Movie_Ticket_Booking.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("""
            SELECT new com.org.Movie_Ticket_Booking.dto.respone.ReviewRespone(
            r.id, r.movie.id, u.name, u.email, r.comment, r.rating, r.reviewDate, r.isDisplay)
            FROM Review r join r.user u
            where  r.movie.id = :movieId
            ORDER BY r.reviewDate DESC
            """)
    Page<ReviewRespone> findReviewsByMovieId(Long movieId, Pageable pageable);
}
