package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Theater;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TheaterRepository extends MongoRepository<Theater, Integer> {
}
