package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Showtime;
import movie.ticket.movie_ticket_booking.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class ShowtimeListener extends AbstractMongoEventListener<Showtime> {

    private final PrimarySequenceService primarySequenceService;

    public ShowtimeListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Showtime> event) {
        if (event.getSource().getShowtimeId() == null) {
            event.getSource().setShowtimeId(((int)primarySequenceService.getNextValue()));
        }
    }

}
