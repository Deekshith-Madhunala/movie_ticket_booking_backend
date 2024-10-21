package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Booking;
import movie.ticket.movie_ticket_booking.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PaymentRepository extends MongoRepository<Payment, Integer> {

    Payment findFirstByBooking(Booking booking);

}
