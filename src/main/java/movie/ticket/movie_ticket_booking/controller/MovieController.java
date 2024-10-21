package movie.ticket.movie_ticket_booking.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import movie.ticket.movie_ticket_booking.modelDTO.MovieDTO;
import movie.ticket.movie_ticket_booking.service.MovieService;
import movie.ticket.movie_ticket_booking.util.ReferencedException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/api/movies", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

    private final MovieService movieService;

    public MovieController(final MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.findAll());
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDTO> getMovie(
            @PathVariable(name = "movieId") final Integer movieId) {
        return ResponseEntity.ok(movieService.get(movieId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createMovie(@RequestBody @Valid final MovieDTO movieDTO) {
        final Integer createdMovieId = movieService.create(movieDTO);
        return new ResponseEntity<>(createdMovieId, HttpStatus.CREATED);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<Integer> updateMovie(
            @PathVariable(name = "movieId") final Integer movieId,
            @RequestBody @Valid final MovieDTO movieDTO) {
        movieService.update(movieId, movieDTO);
        return ResponseEntity.ok(movieId);
    }

    @DeleteMapping("/{movieId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMovie(@PathVariable(name = "movieId") final Integer movieId) {
        final ReferencedWarning referencedWarning = movieService.getReferencedWarning(movieId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        movieService.delete(movieId);
        return ResponseEntity.noContent().build();
    }

}
