package movie.ticket.movie_ticket_booking.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import movie.ticket.movie_ticket_booking.modelDTO.TimeSlotDTO;
import movie.ticket.movie_ticket_booking.service.TimeSlotService;
import movie.ticket.movie_ticket_booking.util.ReferencedException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/api/timeSlots", produces = MediaType.APPLICATION_JSON_VALUE)
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(final TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @GetMapping
    public ResponseEntity<List<TimeSlotDTO>> getAllUsers() {
        return ResponseEntity.ok(timeSlotService.findAll());
    }

    @GetMapping("/{timeSlotId}")
    public ResponseEntity<TimeSlotDTO> getTimeSlot(@PathVariable(name = "timeSlotId") final Integer timeSlotId) {
        return ResponseEntity.ok(timeSlotService.get(timeSlotId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createTimeSlot(@RequestBody @Valid final TimeSlotDTO timeSlotDTO) {
        log.info("received timeSlot = {}", timeSlotDTO.getTimeSlot());
        final Integer createdTimeSlotId = timeSlotService.create(timeSlotDTO);
        return new ResponseEntity<>(createdTimeSlotId, HttpStatus.CREATED);
    }

    @PutMapping("/{timeSlotId}")
    public ResponseEntity<Integer> updateTimeSlot(@PathVariable(name = "timeSlotId") final Integer timeSlotId,
                                                  @RequestBody @Valid final TimeSlotDTO timeSlotDTO) {
        timeSlotService.update(timeSlotId, timeSlotDTO);
        return ResponseEntity.ok(timeSlotId);
    }

    @DeleteMapping("/{timeSlotId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable(name = "timeSlotId") final Integer timeSlotId) {
        final ReferencedWarning referencedWarning = timeSlotService.getReferencedWarning(timeSlotId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        timeSlotService.delete(timeSlotId);
        return ResponseEntity.noContent().build();
    }

}
