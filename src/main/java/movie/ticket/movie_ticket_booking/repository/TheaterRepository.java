package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Theater;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TheaterRepository extends MongoRepository<Theater, ObjectId> {
    Theater findByTheaterId(Integer theaterId);

    void deleteByTheaterId(Integer theaterId);
}
