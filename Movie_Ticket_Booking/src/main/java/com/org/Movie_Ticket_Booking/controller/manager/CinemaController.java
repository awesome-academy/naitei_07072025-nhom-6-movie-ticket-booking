package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Room;
import com.org.Movie_Ticket_Booking.entity.Seat;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.service.CinemaService;
import com.org.Movie_Ticket_Booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/cinema")
public class CinemaController {

    private CinemaService cinemaService;
    private RoomService roomService;

    @Autowired
    public CinemaController(CinemaService cinemaService, RoomService roomService) {
        this.cinemaService = cinemaService;
        this.roomService = roomService;}


    @GetMapping("/cinemas")
    public String getCinemasForUser(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        if (user.getRoles().stream()
                .anyMatch(role -> "CINEMA_MANAGER".equals(role.getName()))) {
            // chỉ load rạp manager quản lý
            List<Cinema> cinemas = cinemaService.findByManagerId(user.getId());
            model.addAttribute("cinemas", cinemas);
        }
        return "CinemaManager/cinema";
    }

    @GetMapping("/{cinemaId}/rooms")
    @ResponseBody
    public List<Room> getRoomsByCinemaId(@PathVariable Long cinemaId) {
        return roomService.findRoomByCinemaId(cinemaId);
    }
}
