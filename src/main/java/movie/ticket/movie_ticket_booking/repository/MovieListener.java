package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Movie;
import movie.ticket.movie_ticket_booking.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class MovieListener extends AbstractMongoEventListener<Movie> {

    private final PrimarySequenceService primarySequenceService;

    public MovieListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Movie> event) {
        if (event.getSource().getMovieId() == null) {
            event.getSource().setMovieId(((int)primarySequenceService.getNextValue()));
        }
    }

}
