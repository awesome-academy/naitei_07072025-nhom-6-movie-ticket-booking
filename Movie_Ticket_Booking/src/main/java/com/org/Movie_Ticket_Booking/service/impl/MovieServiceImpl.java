package com.org.Movie_Ticket_Booking.service.impl;

import com.org.Movie_Ticket_Booking.dto.request.MovieRequest;
import com.org.Movie_Ticket_Booking.dto.respone.MovieResponse;
import com.org.Movie_Ticket_Booking.dto.respone.MovieListItemResponse;
import com.org.Movie_Ticket_Booking.dto.respone.PagedMoviesResponse;
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
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

        for (int i = 1; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.isEmpty()) continue;

            Movie movie = buildMovieFromRow(row);

            Optional<Movie> existing = movieRepository.findByTitleAndReleaseDate(movie.getTitle(), movie.getReleaseDate());

            if (existing.isPresent()) {
                duplicates.add(movieMapper.toMovieRespone(existing.get()));
                movie.setId(existing.get().getId()); // assign old ID để update
                duplicatesEntities.add(movie);
            } else {
                newMovies.add(movie);
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

        LocalDate releaseDate = LocalDate.parse(releaseDateStr);

        return Movie.builder()
                .title(title)
                .description(description)
                .releaseDate(releaseDate)
                .duration(Integer.parseInt(durationStr))
                .posterUrl(posterUrl)
                .language(language)
                .genres(resolveGenres(genres))
                .build();
    }

    private boolean isValidDate(String dateStr) {
        return dateStr != null && dateStr.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    private void clearImportSession(HttpSession session) {
        session.removeAttribute("newMovies");
        session.removeAttribute("duplicateMovies");
    }

    // Search movie
    @Override
    public PagedMoviesResponse searchMovies(
            String title,
            List<String> genres,
            String dateFrom,
            String dateTo,
            String startTime,
            String endTime,
            String language,
            int page,
            int size
    ) {
        Specification<Movie> spec = Specification.allOf();

        // Fuzzy search theo title
        if (title != null && !title.isBlank()) {
            String like = "%" + title.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), like)
            );
        }

        // Filter theo ngôn ngữ
        if (language != null && !language.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("language")), language.trim().toLowerCase())
            );
        }

        // Filter theo genre (multi-select)
        if (genres != null && !genres.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                var join = root.join("genres");
                List<String> lowered = genres.stream()
                        .filter(s -> s != null && !s.isBlank())
                        .map(s -> s.trim().toLowerCase())
                        .toList();
                query.distinct(true);
                return cb.lower(join.get("name")).in(lowered);
            });
        }

        // Filter theo khoảng ngày chiếu (dateFrom - dateTo)
        if ((dateFrom != null && !dateFrom.isBlank()) || (dateTo != null && !dateTo.isBlank())) {
            LocalDate from = (dateFrom == null || dateFrom.isBlank()) ? null : LocalDate.parse(dateFrom);
            LocalDate to = (dateTo == null || dateTo.isBlank()) ? null : LocalDate.parse(dateTo);

            spec = spec.and((root, query, cb) -> {
                var stJoin = root.join("showtimes");
                query.distinct(true);
                if (from != null && to != null) {
                    return cb.between(stJoin.get("date"), from, to);
                } else if (from != null) {
                    return cb.greaterThanOrEqualTo(stJoin.get("date"), from);
                } else {
                    return cb.lessThanOrEqualTo(stJoin.get("date"), to);
                }
            });
        }

        //  Filter theo giờ chiếu (startTime - endTime)
        if ((startTime != null && !startTime.isBlank()) || (endTime != null && !endTime.isBlank())) {
            LocalTime start = (startTime == null || startTime.isBlank()) ? null : LocalTime.parse(startTime);
            LocalTime end = (endTime == null || endTime.isBlank()) ? null : LocalTime.parse(endTime);

            spec = spec.and((root, query, cb) -> {
                var stJoin = root.join("showtimes");
                query.distinct(true);
                if (start != null && end != null) {
                    return cb.between(stJoin.get("startTime"), start, end);
                } else if (start != null) {
                    return cb.greaterThanOrEqualTo(stJoin.get("startTime"), start);
                } else {
                    return cb.lessThanOrEqualTo(stJoin.get("startTime"), end);
                }
            });
        }

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "releaseDate"));

        Page<Movie> result = movieRepository.findAll(spec, pageable);

        List<MovieListItemResponse> items = result.getContent().stream()
                .map(movieMapper::toListItem)
                .toList();

        return PagedMoviesResponse.builder()
                .items(items)
                .total(result.getTotalElements())
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalPages(result.getTotalPages())
                .build();
    }
}
