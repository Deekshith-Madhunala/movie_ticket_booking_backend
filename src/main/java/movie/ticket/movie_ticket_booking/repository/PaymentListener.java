package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Payment;
import movie.ticket.movie_ticket_booking.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class PaymentListener extends AbstractMongoEventListener<Payment> {

    private final PrimarySequenceService primarySequenceService;

    public PaymentListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Payment> event) {
        if (event.getSource().getPaymentId() == null) {
            event.getSource().setPaymentId(((int)primarySequenceService.getNextValue()));
        }
    }

}
