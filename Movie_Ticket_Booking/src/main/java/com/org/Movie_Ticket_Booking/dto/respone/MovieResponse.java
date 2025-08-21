package com.org.Movie_Ticket_Booking.dto.respone;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponse {
    private String title;
    private String description;
    private Integer duration;
    private String language;
    private String posterUrl;
    private String releaseDate;
    private List<String> genres;
}
