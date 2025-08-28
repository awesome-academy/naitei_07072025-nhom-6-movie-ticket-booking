package com.org.Movie_Ticket_Booking.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRespone {
    private Long id;
    private Long movieID;
    private String userName;
    private String email;
    private String comment;
    private Integer rating;
    private LocalDateTime reviewDate;
    private Integer isDisplay;
}
