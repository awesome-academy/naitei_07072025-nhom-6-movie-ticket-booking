package com.org.Movie_Ticket_Booking.service;

import com.org.Movie_Ticket_Booking.dto.respone.MovieResponse;
import com.org.Movie_Ticket_Booking.dto.respone.PagedMoviesResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {
    List<MovieResponse> importMovies(MultipartFile file, HttpSession session) throws IOException;
    void saveAllImported(HttpSession session, boolean overwrite);
    void cancelImport(HttpSession session);
    Page<MovieResponse> getListMovies(Pageable pageable);
    MovieResponse getMovieDetail(Long id);

    PagedMoviesResponse searchMovies(
            String title,
            List<String> genres,
            String dateFrom,
            String dateTo,
            String startTime,
            String endTime,
            String language,
            int page,
            int size
    );
}
