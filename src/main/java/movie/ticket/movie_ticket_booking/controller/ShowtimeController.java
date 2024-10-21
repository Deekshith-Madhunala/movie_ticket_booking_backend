package movie.ticket.movie_ticket_booking.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import movie.ticket.movie_ticket_booking.modelDTO.ShowtimeDTO;
import movie.ticket.movie_ticket_booking.service.ShowtimeService;
import movie.ticket.movie_ticket_booking.util.ReferencedException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/showtimes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    public ShowtimeController(final ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping
    public ResponseEntity<List<ShowtimeDTO>> getAllShowtimes() {
        return ResponseEntity.ok(showtimeService.findAll());
    }

    @GetMapping("/{showtimeId}")
    public ResponseEntity<ShowtimeDTO> getShowtime(
            @PathVariable(name = "showtimeId") final Integer showtimeId) {
        return ResponseEntity.ok(showtimeService.get(showtimeId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createShowtime(
            @RequestBody @Valid final ShowtimeDTO showtimeDTO) {
        final Integer createdShowtimeId = showtimeService.create(showtimeDTO);
        return new ResponseEntity<>(createdShowtimeId, HttpStatus.CREATED);
    }

    @PutMapping("/{showtimeId}")
    public ResponseEntity<Integer> updateShowtime(
            @PathVariable(name = "showtimeId") final Integer showtimeId,
            @RequestBody @Valid final ShowtimeDTO showtimeDTO) {
        showtimeService.update(showtimeId, showtimeDTO);
        return ResponseEntity.ok(showtimeId);
    }

    @DeleteMapping("/{showtimeId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteShowtime(
            @PathVariable(name = "showtimeId") final Integer showtimeId) {
        final ReferencedWarning referencedWarning = showtimeService.getReferencedWarning(showtimeId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        showtimeService.delete(showtimeId);
        return ResponseEntity.noContent().build();
    }

}