package movie.ticket.movie_ticket_booking.repository;

import jakarta.validation.constraints.NotNull;
import movie.ticket.movie_ticket_booking.entity.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TimeSlotRepository extends MongoRepository<TimeSlot, String> {
    void deleteByTimeSlotId(Integer userId);

    TimeSlot findByTimeSlotId(Integer userId);

    List<TimeSlot> findAllByTimeSlotIdIn(@NotNull List<Integer> timeSlotIds);
}
