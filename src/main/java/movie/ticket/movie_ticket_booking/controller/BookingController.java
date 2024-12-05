package movie.ticket.movie_ticket_booking.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import movie.ticket.movie_ticket_booking.modelDTO.BookingDTO;
import movie.ticket.movie_ticket_booking.modelDTO.BookingDetailsDTO;
import movie.ticket.movie_ticket_booking.service.BookingService;
import movie.ticket.movie_ticket_booking.util.ReferencedException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {

    private final BookingService bookingService;

    public BookingController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBooking(
            @PathVariable(name = "bookingId") final Integer bookingId) {
        return ResponseEntity.ok(bookingService.get(bookingId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createBooking(@RequestBody @Valid final BookingDTO bookingDTO) {
        final Integer createdBookingId = bookingService.create(bookingDTO);
        return new ResponseEntity<>(createdBookingId, HttpStatus.CREATED);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<Integer> updateBooking(
            @PathVariable(name = "bookingId") final Integer bookingId,
            @RequestBody @Valid final BookingDTO bookingDTO) {
        bookingService.update(bookingId, bookingDTO);
        return ResponseEntity.ok(bookingId);
    }

    @PostMapping("/status/{bookingId}")
    public ResponseEntity<Integer> updateBookingStatus(
            @PathVariable(name = "bookingId") final Integer bookingId) {
        bookingService.updateBookingStatus(bookingId);
        return ResponseEntity.ok(bookingId);
    }

    @DeleteMapping("/{bookingId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable(name = "bookingId") final Integer bookingId) {
        final ReferencedWarning referencedWarning = bookingService.getReferencedWarning(bookingId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        bookingService.delete(bookingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Document>> getBookingDetailsByUserId(@PathVariable String userId) {
        List<Document> bookings = bookingService.getBookingDetailsByUserId(userId);
        if (bookings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

}
