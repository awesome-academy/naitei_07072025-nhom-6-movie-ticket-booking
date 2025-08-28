package com.org.Movie_Ticket_Booking.controller.manager;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import com.org.Movie_Ticket_Booking.dto.respone.MovieStatisticsResponse;
import com.org.Movie_Ticket_Booking.dto.respone.RevenueStatisticsResponse;
import com.org.Movie_Ticket_Booking.dto.respone.TimeSlotStatisticsResponse;
import com.org.Movie_Ticket_Booking.entity.User;
import com.org.Movie_Ticket_Booking.config.CustomUserDetail;
import com.org.Movie_Ticket_Booking.service.CinemaService;
import com.org.Movie_Ticket_Booking.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StatisticsController extends ManagerController {

    private StatisticsService statisticsService;
    private CinemaService cinemaService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, CinemaService cinemaService) {
        this.statisticsService = statisticsService;
        this.cinemaService = cinemaService;
    }

    @GetMapping("/statistics")
    public String showStatisticsPage(Authentication authentication, Model model) {
        try {
            CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userDetail.getUser();

            System.out.println("User ID: " + user.getId()); // Debug log

            model.addAttribute("pageTitle", "Thống kê & Báo cáo");
            model.addAttribute("activePage", "statistics");
            model.addAttribute("content", ViewNames.MANAGER_STATISTICS);

            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(30);

            model.addAttribute("startDate", startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            model.addAttribute("endDate", endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            model.addAttribute("managerId", user.getId());

            // Test this line specifically
            try {
                model.addAttribute("cinemas", cinemaService.getCinemasByManager(user.getId()));
            } catch (Exception e) {
                System.err.println("Error loading cinemas: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("cinemas", new ArrayList<>());
            }

            return ViewNames.LAYOUT_MANAGER;
        } catch (Exception e) {
            System.err.println("Error in showStatisticsPage: " + e.getMessage());
            e.printStackTrace();
            return "error"; // hoặc redirect đến trang error
        }
    }

    @GetMapping("/statistics/movies")
    @ResponseBody
    public List<MovieStatisticsResponse> getMovieStatistics(
            Authentication authentication,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long cinemaId
    ) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetail.getUser();

        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();

        return statisticsService.getMovieStatisticsByManager(user.getId(), cinemaId, start, end);
    }


    @GetMapping("/statistics/timeslots")
    @ResponseBody
    public List<TimeSlotStatisticsResponse> getTimeSlotStatistics(
            Authentication authentication,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long cinemaId
    ) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetail.getUser();

        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();

        return statisticsService.getTimeSlotStatisticsByManager(user.getId(), cinemaId, start, end);
    }

    @GetMapping("/statistics/revenue")
    @ResponseBody
    public List<RevenueStatisticsResponse> getRevenueStatistics(
            Authentication authentication,
            @RequestParam String period, // day, month, year
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long cinemaId
    ) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetail.getUser();

        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();

        return statisticsService.getRevenueStatisticsByManager(user.getId(), cinemaId, period, start, end);
    }
}
