package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.dto.respone.ReviewRespone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Page<ReviewRespone> getAllReviews(Long movieID, Pageable pageable);
    public int toggleDisplay(Long id);
}
