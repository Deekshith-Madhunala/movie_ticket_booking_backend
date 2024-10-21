package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MovieRepository extends MongoRepository<Movie, Integer> {
}
