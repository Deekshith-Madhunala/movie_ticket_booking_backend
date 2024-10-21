package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Booking;
import movie.ticket.movie_ticket_booking.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class BookingListener extends AbstractMongoEventListener<Booking> {

    private final PrimarySequenceService primarySequenceService;

    public BookingListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Booking> event) {
        if (event.getSource().getBookingId() == null) {
            event.getSource().setBookingId(((int)primarySequenceService.getNextValue()));
        }
    }

}
