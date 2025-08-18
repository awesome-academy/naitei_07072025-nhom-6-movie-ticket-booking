package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.entity.Room;
import com.org.Movie_Ticket_Booking.entity.Seat;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/room")
public class RoomController {
    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService){
        this.roomService=roomService;
    }

    @GetMapping("/{roomId}/seats")
    public String getAllSeat(@PathVariable Long roomId, Model model) {
        Room room = roomService.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.UNIDENTIFIED_ERROR));
        room.getSeats().forEach(seat -> seat.getTypeSeat().getName());
        Set<Seat> seats =room.getSeats();
        model.addAttribute("seats", seats);
        return "CinemaManager/seats";
    }
}
