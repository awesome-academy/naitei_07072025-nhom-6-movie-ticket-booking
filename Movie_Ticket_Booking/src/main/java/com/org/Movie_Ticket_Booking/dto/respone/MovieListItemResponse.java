package com.org.Movie_Ticket_Booking.dto.respone;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieListItemResponse {
    private Long id;
    private String title;
    private String poster_url;
    private String release_date;
}
