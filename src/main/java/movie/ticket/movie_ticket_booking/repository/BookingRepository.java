package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Booking;
import movie.ticket.movie_ticket_booking.entity.Showtime;
import movie.ticket.movie_ticket_booking.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookingRepository extends MongoRepository<Booking, String> {

    Booking findFirstByUser(User user);

    Booking findFirstByShowtime(Showtime showtime);

    Booking findByBookingId(Integer bookingId);

    void deleteByBookingId(Integer bookingId);
}
