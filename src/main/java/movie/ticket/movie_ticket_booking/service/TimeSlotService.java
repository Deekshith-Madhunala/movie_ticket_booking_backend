package movie.ticket.movie_ticket_booking.service;

import lombok.extern.slf4j.Slf4j;
import movie.ticket.movie_ticket_booking.entity.Booking;
import movie.ticket.movie_ticket_booking.entity.TimeSlot;
import movie.ticket.movie_ticket_booking.entity.User;
import movie.ticket.movie_ticket_booking.modelDTO.TimeSlotDTO;
import movie.ticket.movie_ticket_booking.repository.TimeSlotRepository;
import movie.ticket.movie_ticket_booking.util.NotFoundException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Slf4j
@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    public List<TimeSlotDTO> findAll() {
        final List<TimeSlot> timeSlots = timeSlotRepository.findAll(Sort.by("timeSlot"));
        return timeSlots.stream()
                .map(timeSlot -> mapToDTO(timeSlot, new TimeSlotDTO()))
                .toList();
    }

    public TimeSlotDTO get(final Integer timeSlotId) {
        TimeSlot timeSlot = timeSlotRepository.findByTimeSlotId(timeSlotId);
        if (timeSlot == null) {
            throw new NotFoundException();
        }
        return mapToDTO(timeSlot, new TimeSlotDTO());
    }

    public Integer create(final TimeSlotDTO timeSlotDTO) {
        final TimeSlot timeSlot = new TimeSlot();
        mapToEntity(timeSlotDTO, timeSlot);
        return timeSlotRepository.save(timeSlot).getTimeSlotId();
    }

    public void update(final Integer userId, final TimeSlotDTO timeSlotDTO) {
        final TimeSlot timeSlot = timeSlotRepository.findByTimeSlotId(userId);
        if (timeSlot == null) {
            throw new NotFoundException();
        }
        mapToEntity(timeSlotDTO, timeSlot);
        timeSlotRepository.save(timeSlot);
    }

    public void delete(final Integer userId) {
        timeSlotRepository.deleteByTimeSlotId(userId);
    }

    private TimeSlotDTO mapToDTO(final TimeSlot timeSlot, final TimeSlotDTO timeSlotDTO) {
        timeSlotDTO.setId(timeSlot.getId());
        timeSlotDTO.setTimeSlotId(timeSlot.getTimeSlotId());
        timeSlotDTO.setTimeSlot(timeSlot.getTimeSlot());
        return timeSlotDTO;
    }

    private TimeSlot mapToEntity(final TimeSlotDTO timeSlotDTO, final TimeSlot timeSlot) {

        timeSlot.setId(timeSlotDTO.getId());
        timeSlot.setTimeSlotId(timeSlotDTO.getTimeSlotId());
        timeSlot.setTimeSlot(timeSlotDTO.getTimeSlot());
        log.info("set the time slot to entity: {}, {}, {}", timeSlot.getId(), timeSlot.getTimeSlotId(), timeSlot.getTimeSlot());
        return timeSlot;
    }

    public ReferencedWarning getReferencedWarning(final Integer timeSlotId) {
        final TimeSlot timeSlot = timeSlotRepository.findByTimeSlotId(timeSlotId);
        if (timeSlot == null) {
            throw new NotFoundException();
        }
        return null;
    }

}
