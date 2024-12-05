package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserId(Integer userId);

    void deleteByUserId(Integer userId);

    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);
}
