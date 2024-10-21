package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Booking;
import movie.ticket.movie_ticket_booking.entity.Showtime;
import movie.ticket.movie_ticket_booking.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookingRepository extends MongoRepository<Booking, Integer> {

    Booking findFirstByUser(User user);

    Booking findFirstByShowtime(Showtime showtime);

}
