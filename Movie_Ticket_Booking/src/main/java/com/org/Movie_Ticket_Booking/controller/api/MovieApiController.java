package com.org.Movie_Ticket_Booking.controller.api;


import com.org.Movie_Ticket_Booking.dto.respone.PagedMoviesResponse;
import com.org.Movie_Ticket_Booking.service.MovieService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MovieApiController {


    private final MovieService movieService;


    // GET /api/movies
    @GetMapping("/movies")
    public PagedMoviesResponse searchMovies(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        List<String> genres = null;
        if (genre != null && !genre.isBlank()) {
            genres = Arrays.stream(genre.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }

        return movieService.searchMovies(title, genres, startDate, endDate, startTime, endTime, language, page, size);
    }

}
