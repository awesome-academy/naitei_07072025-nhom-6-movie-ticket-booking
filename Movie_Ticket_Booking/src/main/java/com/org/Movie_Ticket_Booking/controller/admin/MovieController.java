package com.org.Movie_Ticket_Booking.controller.admin;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import com.org.Movie_Ticket_Booking.dto.respone.MovieResponse;
import com.org.Movie_Ticket_Booking.service.MovieService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController extends AdminController{
    private final MovieService movieService;

    @GetMapping("/movies")
    public String movies(Model model){
        model.addAttribute("activePage", "movies");
        model.addAttribute("content", ViewNames.CONTENT_MOVIES);
        return ViewNames.LAYOUT_ADMIN;
    }

    @PostMapping("/movies")
    public String importMovies(@RequestParam("file") MultipartFile file,
                               HttpSession session,
                               Model model) throws IOException {

        List<MovieResponse> duplicates = movieService.importMovies(file, session);

        if (!duplicates.isEmpty()) {
            model.addAttribute("duplicates", duplicates);
            return ViewNames.CONFIRM_IMPORT_MOVIE; // Thymeleaf template
        }

        movieService.saveAllImported(session, false);
        return "redirect:/admin/movies?success";
    }

    @PostMapping("/movies/import/submit")
    public String submitImport(@RequestParam("overwrite") boolean overwrite,
                               HttpSession session) {
        movieService.saveAllImported(session, overwrite);
        return "redirect:/admin/movies?importSuccess";
    }

    @PostMapping("/movies/import/cancel")
    public String cancelImport(HttpSession session) {
        movieService.cancelImport(session);
        return "redirect:/admin/movies?importCanceled";
    }
}
