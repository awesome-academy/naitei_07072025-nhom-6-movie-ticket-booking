package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.config.CustomUserDetail;
import com.org.Movie_Ticket_Booking.constants.ViewNames;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
public class CinemaController extends ManagerController{

    private CinemaService cinemaService;
    private RoomService roomService;

    @Autowired
    public CinemaController(CinemaService cinemaService, RoomService roomService) {
        this.cinemaService = cinemaService;
        this.roomService = roomService;}


    @GetMapping("/cinema")
    public String getCinemasForUser(Authentication authentication, Model model) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetail.getUser();
            // chỉ load rạp manager quản lý
            List<Cinema> cinemas = cinemaService.findByManagerId(user.getId());
            model.addAttribute("cinemas", cinemas);
        model.addAttribute("activePage", "cinemas");
        model.addAttribute("content", ViewNames.MANAGER_CINEMAS);
        return ViewNames.LAYOUT_MANAGER;
    }

    @GetMapping("/{cinemaId}/rooms")
    @ResponseBody
    public List<Room> getRoomsByCinemaId(@PathVariable Long cinemaId) {
        return roomService.findRoomByCinemaId(cinemaId);
    }
}
