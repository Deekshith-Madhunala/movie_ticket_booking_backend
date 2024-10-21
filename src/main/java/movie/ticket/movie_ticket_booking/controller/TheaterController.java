package movie.ticket.movie_ticket_booking.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import movie.ticket.movie_ticket_booking.modelDTO.TheaterDTO;
import movie.ticket.movie_ticket_booking.service.TheaterService;
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
@RequestMapping(value = "/api/theaters", produces = MediaType.APPLICATION_JSON_VALUE)
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(final TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping
    public ResponseEntity<List<TheaterDTO>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.findAll());
    }

    @GetMapping("/{theaterId}")
    public ResponseEntity<TheaterDTO> getTheater(
            @PathVariable(name = "theaterId") final Integer theaterId) {
        return ResponseEntity.ok(theaterService.get(theaterId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createTheater(@RequestBody @Valid final TheaterDTO theaterDTO) {
        final Integer createdTheaterId = theaterService.create(theaterDTO);
        return new ResponseEntity<>(createdTheaterId, HttpStatus.CREATED);
    }

    @PutMapping("/{theaterId}")
    public ResponseEntity<Integer> updateTheater(
            @PathVariable(name = "theaterId") final Integer theaterId,
            @RequestBody @Valid final TheaterDTO theaterDTO) {
        theaterService.update(theaterId, theaterDTO);
        return ResponseEntity.ok(theaterId);
    }

    @DeleteMapping("/{theaterId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTheater(
            @PathVariable(name = "theaterId") final Integer theaterId) {
        final ReferencedWarning referencedWarning = theaterService.getReferencedWarning(theaterId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        theaterService.delete(theaterId);
        return ResponseEntity.noContent().build();
    }

}
