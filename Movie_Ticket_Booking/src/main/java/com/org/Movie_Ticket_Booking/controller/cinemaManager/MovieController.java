package com.org.Movie_Ticket_Booking.controller.cinemaManager;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Movie;
import com.org.Movie_Ticket_Booking.entity.Room;
import com.org.Movie_Ticket_Booking.entity.Showtime;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.service.CinemaService;
import com.org.Movie_Ticket_Booking.service.MovieService;
import com.org.Movie_Ticket_Booking.service.RoomService;
import com.org.Movie_Ticket_Booking.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/movie")
public class MovieController {

    private CinemaService cinemaService;
    private RoomService roomService;
    private MovieService movieService;
    private ShowtimeService showtimeService;

    @Autowired
    public MovieController(CinemaService cinemaService, RoomService roomService, MovieService movieService, ShowtimeService showtimeService) {
        this.cinemaService = cinemaService;
        this.roomService = roomService;
        this.movieService=movieService;
        this.showtimeService=showtimeService;
    }

    @GetMapping("/movies")
    public String UiForManagerMovie(Model model) throws Exception{
        List<Cinema> cinemas = cinemaService.findAll();

        // Lấy danh sách thành phố
        List<String> cities = cinemas.stream()
                .map(Cinema::getAddress)
                .distinct()
                .toList();
        // Map city -> Map<tên rạp, ID rạp>
        Map<String, Map<String, Long>> cityToCinemas = new HashMap<>();
        for (Cinema cinema : cinemas) {
            cityToCinemas.computeIfAbsent(cinema.getAddress(), k -> new HashMap<>())
                    .put(cinema.getName(), cinema.getId());
        }

        model.addAttribute("cities", cities);
        model.addAttribute("cityToCinemas", cityToCinemas);
        return "CinemaManager/movie";
    }

    @GetMapping("/movies/{id}")
    public String movieDetail( @PathVariable Long id, @RequestParam Long cinemaId, Model model) {
        Movie movie = movieService.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        Cinema cinema = cinemaService.findById(cinemaId)
                .orElseThrow(() -> new AppException(ErrorCode.INTERNAL_ERROR));

        // lấy danh sách room của rạp
        List<Room> rooms = roomService.findRoomByCinemaId(cinemaId);
        model.addAttribute("movie", movie);
        model.addAttribute("cinema", cinema);
        model.addAttribute("rooms", rooms);
        return "CinemaManager/movie-detail";
    }

    @GetMapping("/movies/cinema/{cinemaId}")
    @ResponseBody
    public Set<Movie> getMovieByCinemaId(@PathVariable Long cinemaId) {
        Cinema cinema = this.cinemaService.findById(cinemaId).orElseThrow(()->new AppException(ErrorCode.MOVIE_NOT_FOUND));
        return cinema.getMovies();
    }
}
