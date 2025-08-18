package com.org.Movie_Ticket_Booking.controller.cinemaManager;

import com.org.Movie_Ticket_Booking.entity.Cinema;
import com.org.Movie_Ticket_Booking.entity.Room;
import com.org.Movie_Ticket_Booking.service.CinemaService;
import com.org.Movie_Ticket_Booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cinema")
public class CinemaController {

    private CinemaService cinemaService;
    private RoomService roomService;

    @Autowired
    public CinemaController(CinemaService cinemaService, RoomService roomService) {
        this.cinemaService = cinemaService;
        this.roomService = roomService;}

    @GetMapping("/room")
    public String UiForManager(Model model) throws Exception{
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

        return "cinemaManager/cinema";
    }

    @GetMapping("/rooms/{cinemaId}")
    @ResponseBody
    public List<Room> getRoomsByCinemaId(@PathVariable Long cinemaId) {
        return roomService.findRoomByCinemaId(cinemaId);
    }
}
