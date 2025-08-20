package com.org.Movie_Ticket_Booking.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequest {
    private String title;
    private String description;
    private Integer duration;
    private String language;
    private String posterUrl;
    private String releaseDate;
    private String genres;
}
