package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.dto.respone.ReviewRespone;
import com.org.Movie_Ticket_Booking.entity.Review;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.repository.ReviewRepository;
import com.org.Movie_Ticket_Booking.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public Page<ReviewRespone> getAllReviews(Long movieID, Pageable pageable) {
        if (movieID == null) {
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        }
        return reviewRepository.findReviewsByMovieId(movieID, pageable);
    }

    @Override
    public int toggleDisplay(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        int newStatus = review.getIsDisplay() == 1 ? 0 : 1;
        review.setIsDisplay(newStatus);
        reviewRepository.save(review);
        return newStatus;
    }
}
