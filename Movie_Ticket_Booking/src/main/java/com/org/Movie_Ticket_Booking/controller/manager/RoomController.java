package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import com.org.Movie_Ticket_Booking.entity.Room;
import com.org.Movie_Ticket_Booking.entity.Seat;
import com.org.Movie_Ticket_Booking.exception.AppException;
import com.org.Movie_Ticket_Booking.exception.ErrorCode;
import com.org.Movie_Ticket_Booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Controller
@RequestMapping("/rooms")
public class RoomController extends ManagerController{
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

        model.addAttribute("content", ViewNames.MANAGER_SEATS);
        return ViewNames.LAYOUT_MANAGER;
    }

    @PostMapping("/import")
    public String importRoom(@RequestParam("file") MultipartFile file) throws IOException {
            roomService.saveData(file);
            return "redirect:/manager/cinema";
    }
}
