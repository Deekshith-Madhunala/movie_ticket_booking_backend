package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Theater;
import movie.ticket.movie_ticket_booking.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class TheaterListener extends AbstractMongoEventListener<Theater> {

    private final PrimarySequenceService primarySequenceService;

    public TheaterListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Theater> event) {
        if (event.getSource().getTheaterId() == null) {
            event.getSource().setTheaterId(((int)primarySequenceService.getNextValue()));
        }
    }

}
