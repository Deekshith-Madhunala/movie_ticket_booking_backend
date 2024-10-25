package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.TimeSlot;
import movie.ticket.movie_ticket_booking.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class TimeSlotListener extends AbstractMongoEventListener<TimeSlot> {

    private final PrimarySequenceService primarySequenceService;

    public TimeSlotListener(PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<TimeSlot> event) {
        if (event.getSource().getTimeSlotId() == null) {
            event.getSource().setTimeSlotId(((int) primarySequenceService.getNextValue()));
        }
    }

}
