package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.dto.request.MovieRequest;
import com.org.Movie_Ticket_Booking.dto.respone.MovieResponse;
import com.org.Movie_Ticket_Booking.entity.Genre;
import com.org.Movie_Ticket_Booking.entity.Movie;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.mapper.MovieMapper;
import com.org.Movie_Ticket_Booking.repository.GenreRepository;
import com.org.Movie_Ticket_Booking.repository.MovieRepository;
import com.org.Movie_Ticket_Booking.service.MovieService;
import com.org.Movie_Ticket_Booking.utils.FileReader;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final FileReader fileReader;
    private final MovieMapper movieMapper;

    @Transactional
    @Override
    public List<MovieResponse> importMovies(MultipartFile file, HttpSession session) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        List<List<String>> rows = fileReader.readFile(file);
        List<Movie> newMovies = new ArrayList<>();
        List<MovieResponse> duplicates = new ArrayList<>();
        List<Movie> duplicatesEntities = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++){
            List<String> row = rows.get(i);
            System.out.println(rows);

            if(row.isEmpty()) continue;

            Movie movie = buildMovieFromRow(row);

            Optional<Movie> existing = movieRepository.findByTitleAndReleaseDate(movie.getTitle(), movie.getReleaseDate());

            if(existing.isPresent()){
                duplicates.add(movieMapper.toMovieRespone(existing.get())); // is duplicated
                //assign old ID to new version -> when save will update
                movie.setId(existing.get().getId());
                duplicatesEntities.add(movie);
            }else {
                newMovies.add(movie); //record isn't duplicated
            }
        }
        session.setAttribute("newMovies", newMovies);
        session.setAttribute("duplicateMovies", duplicatesEntities);

        return duplicates;
    }

    @Transactional
    @Override
    public void saveAllImported(HttpSession session, boolean overwrite) {
        List<Movie> newMovies = (List<Movie>) session.getAttribute("newMovies");
        List<Movie> duplicateMovies = (List<Movie>) session.getAttribute("duplicateMovies");

        List<Movie> moviesToSave = new ArrayList<>();

        if (newMovies != null) {
            newMovies.forEach(m -> m.setId(null));
            moviesToSave.addAll(newMovies);
        }
        if (overwrite && duplicateMovies != null) moviesToSave.addAll(duplicateMovies);

        if (!moviesToSave.isEmpty()) {
            movieRepository.saveAll(moviesToSave);
        }

        clearImportSession(session);
    }

    @Override
    public void cancelImport(HttpSession session) {
        clearImportSession(session);
    }

    @Transactional
    @Override
    public Page<MovieResponse> getListMovies(Pageable pageable) {
        Page<Movie> movies = movieRepository.findAll(pageable);
        return movieRepository.findAll(pageable)
                .map(movieMapper::toMovieRespone);
    }

    @Transactional
    @Override
    public MovieResponse getMovieDetail(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        return movieMapper.toMovieRespone(movie);
    }

    private Set<Genre> resolveGenres(String genresString) {
        return Arrays.stream(genresString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(name -> genreRepository.findByName(name)
                        .orElseGet(() -> genreRepository.save(Genre.builder().name(name).build())))
                .collect(Collectors.toSet());
    }

    private Movie buildMovieFromRow(List<String> row) {
        if (row.size() < 7) {
            throw new AppException(ErrorCode.MISSING_DATA_COLUMN);
        }

        String title = row.get(0);
        String description = row.get(1);
        String releaseDateStr = row.get(2);
        String durationStr = row.get(3);
        String posterUrl = row.get(4);
        String language = row.get(5);
        String genres = row.get(6);

        if (title.isBlank()) {
            throw new AppException(ErrorCode.MOVIE_TITLE_NULL);
        }

        if (!isValidDate(releaseDateStr)) {
            throw new AppException(ErrorCode.DATE_INVALID);
        }

        if (!isNumeric(durationStr)) {
            throw new AppException(ErrorCode.DURATION_INVALID);
        }

        MovieRequest request = MovieRequest.builder()
                .title(title)
                .description(description)
                .releaseDate(releaseDateStr)
                .duration(Integer.parseInt(durationStr))
                .posterUrl(posterUrl)
                .language(language)
                .genres(genres)
                .build();

        Movie movie = movieMapper.toEntity(request);
        movie.setGenres(resolveGenres(genres));
        return movie;
    }

    private boolean isValidDate(String dateStr) {
        return dateStr != null && dateStr.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    @SuppressWarnings("unchecked")
    private List<Movie> getSessionList(HttpSession session, String attrName) {
        return (List<Movie>) session.getAttribute(attrName);
    }

    private void clearImportSession(HttpSession session) {
        session.removeAttribute("newMovies");
        session.removeAttribute("duplicateMovies");
    }
}
