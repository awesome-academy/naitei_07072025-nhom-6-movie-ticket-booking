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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/showtime")
public class ShowtimeController {

    private CinemaService cinemaService;
    private RoomService roomService;
    private MovieService movieService;
    private ShowtimeService showtimeService;

    @Autowired
    public ShowtimeController(CinemaService cinemaService, RoomService roomService, MovieService movieService, ShowtimeService showtimeService) {
        this.cinemaService = cinemaService;
        this.roomService = roomService;
        this.movieService=movieService;
        this.showtimeService=showtimeService;
    }

    @Transactional
    @PostMapping("/add")
    public String addShowtime(
            @RequestParam("movieId") Long movieId,
            @RequestParam("cinemaId") Long cinemaId,
            @RequestParam("roomId") Long roomId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            @RequestParam("price") BigDecimal price
    ) {
        // Lấy entity cần thiết
        Movie movie = movieService.findById(movieId).orElseThrow(()->new AppException(ErrorCode.MOVIE_NOT_FOUND));
        Cinema cinema = cinemaService.findById(cinemaId).orElseThrow(()->new AppException(ErrorCode.UNIDENTIFIED_ERROR));
        Room room = roomService.findById(roomId).orElseThrow(()->new AppException(ErrorCode.UNIDENTIFIED_ERROR));

        // Tạo Showtime mới
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setDate(date);
        showtime.setStartTime(time);
        // endTime = startTime + duration của phim
        if (movie.getDuration() != null) {
            showtime.setEndTime(time.plusMinutes(movie.getDuration()));
        }
        showtime.setPrice(price);

        // Lưu vào DB
        showtimeService.addShowtime(showtime);

        // Redirect về lại trang detail của phim
        return "redirect:/movie/movies/" + movieId+ "?cinemaId=" + cinemaId;
    }
}
