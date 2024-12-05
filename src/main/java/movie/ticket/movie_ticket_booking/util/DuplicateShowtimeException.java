package movie.ticket.movie_ticket_booking.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateShowtimeException extends RuntimeException {
    public DuplicateShowtimeException(String message) {
        super(message);
    }
}
