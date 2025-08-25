package com.org.Movie_Ticket_Booking.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedMoviesResponse {
    private List<MovieListItemResponse> items;
    private long total;
    private int page;
    private int size;
    private int totalPages;
}
