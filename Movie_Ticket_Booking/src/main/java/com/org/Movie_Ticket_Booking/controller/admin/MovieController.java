package com.org.Movie_Ticket_Booking.controller.admin;

import com.org.Movie_Ticket_Booking.constants.ViewNames;
import com.org.Movie_Ticket_Booking.dto.respone.MovieResponse;
import com.org.Movie_Ticket_Booking.dto.respone.ReviewRespone;
import com.org.Movie_Ticket_Booking.service.MovieService;
import com.org.Movie_Ticket_Booking.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MovieController extends AdminController{
    private final MovieService movieService;
    private final ReviewService reviewService;

    @GetMapping("/movies")
    public String movies(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model){
        Page<MovieResponse> moviesPage;
        moviesPage = movieService.getListMovies(PageRequest.of(page, size));

        model.addAttribute("activePage", "movies");
        model.addAttribute("content", ViewNames.CONTENT_MOVIES);
        model.addAttribute("movies", moviesPage);
        return ViewNames.LAYOUT_ADMIN;
    }

    @GetMapping("/movies/{id}")
    public String getMovieDetail(@PathVariable Long id,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "false") boolean showReviews,
                                 Model model){
        MovieResponse movie = movieService.getMovieDetail(id);

        Page<ReviewRespone> reviewPage;
        reviewPage = reviewService.getAllReviews(id, PageRequest.of(page, size));
        model.addAttribute("movie", movie);
        model.addAttribute("content", ViewNames.CONTENT_MOVIE_DETAIL);
        model.addAttribute("reviews", reviewPage);
        model.addAttribute("showReviews", showReviews);
        return ViewNames.LAYOUT_ADMIN;
    }

    @PutMapping("/movies/reviews/{id}/toggle")
    @ResponseBody
    public Map<String, Object> toggleReview(@PathVariable Long id) {
        int newStatus = reviewService.toggleDisplay(id);
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("isDisplay", newStatus);
        return result;
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
