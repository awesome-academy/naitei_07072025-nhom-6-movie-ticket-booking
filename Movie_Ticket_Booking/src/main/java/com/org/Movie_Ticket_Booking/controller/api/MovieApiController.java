package com.org.Movie_Ticket_Booking.controller.api;

import com.org.Movie_Ticket_Booking.dto.respone.ApiResponse;
import com.org.Movie_Ticket_Booking.dto.respone.PagedMoviesResponse;
import com.org.Movie_Ticket_Booking.service.MovieService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MovieApiController {

    private final MovieService movieService;

    @GetMapping("/movies")
    public ApiResponse<PagedMoviesResponse> searchMovies(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genres", required = false) List<String> genres,
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        PagedMoviesResponse result = movieService.searchMovies(
                title, genres, dateFrom, dateTo, startTime, endTime, language, page, size
        );

        return ApiResponse.success("Movies retrieved successfully", result);
    }
}
