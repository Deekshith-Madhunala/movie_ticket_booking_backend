package movie.ticket.movie_ticket_booking.modelDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class BookingDetailsDTO {
    private Integer bookingId; // Ensure this matches the type of the ID
    private String paymentStatus;
    private String bookingStatus;
    private BigDecimal totalAmount;
    private OffsetDateTime createdAt;
    private OffsetDateTime cancelledAt;
    private Integer showtimeId; // Make sure this field exists
    private ShowtimeDTO showtime; // This should match your existing ShowtimeDTO
    private Integer movieId; // Make sure this field exists
    private MovieDTO movie; // This should match your existing MovieDTO
    private Integer theaterId; // Make sure this field exists
    private TheaterDTO theater; // This should match your existing TheaterDTO
}
