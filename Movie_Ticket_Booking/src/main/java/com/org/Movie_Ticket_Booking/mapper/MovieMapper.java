package com.org.Movie_Ticket_Booking.mapper;

import com.org.Movie_Ticket_Booking.dto.request.MovieRequest;
import com.org.Movie_Ticket_Booking.dto.respone.MovieListItemResponse;
import com.org.Movie_Ticket_Booking.dto.respone.MovieResponse;
import com.org.Movie_Ticket_Booking.entity.Genre;
import com.org.Movie_Ticket_Booking.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "Spring")
public interface MovieMapper {
    @Mapping(target = "genres", ignore = true)
    Movie toEntity (MovieRequest dto);

    @Mapping(source = "id", target = "id")
    @Mapping(target = "genres", source = "genres")
    MovieResponse toMovieRespone (Movie movie);
    default List<String> mapGenres(Set<Genre> genres) {
        if (genres == null) return Collections.emptyList();
        return genres.stream()
                .map(Genre::getName)
                .toList();
    }

    // map cho list item  (id, title, poster_url, release_date)
    default MovieListItemResponse toListItem(Movie movie) {
        if (movie == null) return null;
        return MovieListItemResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .poster_url(movie.getPosterUrl())
                .release_date(movie.getReleaseDate() == null
                        ? null
                        : movie.getReleaseDate().format(DateTimeFormatter.ISO_DATE))
                .build();
    }
}
