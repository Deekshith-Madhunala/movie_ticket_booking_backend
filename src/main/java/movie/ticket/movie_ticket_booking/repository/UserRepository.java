package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, Integer> {
}
