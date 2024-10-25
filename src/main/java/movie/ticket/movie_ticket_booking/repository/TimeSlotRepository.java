package movie.ticket.movie_ticket_booking.repository;

import movie.ticket.movie_ticket_booking.entity.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeSlotRepository extends MongoRepository<TimeSlot, String> {
    void deleteByTimeSlotId(Integer userId);

    TimeSlot findByTimeSlotId(Integer userId);
}
