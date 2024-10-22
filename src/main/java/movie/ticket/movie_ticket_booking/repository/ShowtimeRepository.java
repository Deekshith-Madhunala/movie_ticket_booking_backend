package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.Movie;
import movie.ticket.movie_ticket_booking.entity.Showtime;
import movie.ticket.movie_ticket_booking.entity.Theater;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ShowtimeRepository extends MongoRepository<Showtime, ObjectId> {

    Showtime findFirstByMovie(Movie movie);

    Showtime findFirstByTheater(Theater theater);

    Showtime findByShowtimeId(Integer showtimeId);

    void deleteByShowtimeId(Integer showtimeId);
}
