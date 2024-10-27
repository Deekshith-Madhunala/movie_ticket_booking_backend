package movie.ticket.movie_ticket_booking.service;

import lombok.extern.slf4j.Slf4j;
import movie.ticket.movie_ticket_booking.entity.*;
import movie.ticket.movie_ticket_booking.modelDTO.ShowtimeDTO;
import movie.ticket.movie_ticket_booking.repository.*;
import movie.ticket.movie_ticket_booking.util.NotFoundException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final BookingRepository bookingRepository;
    private final TimeSlotRepository timeSlotRepository;

    public ShowtimeService(final ShowtimeRepository showtimeRepository,
                           final MovieRepository movieRepository, final TheaterRepository theaterRepository,
                           final BookingRepository bookingRepository, TimeSlotRepository timeSlotRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.bookingRepository = bookingRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<ShowtimeDTO> findAll() {
        final List<Showtime> showtimes = showtimeRepository.findAll(Sort.by("showtimeId"));
        return showtimes.stream()
                .map(showtime -> mapToDTO(showtime, new ShowtimeDTO()))
                .toList();
    }

    public ShowtimeDTO get(final Integer showtimeId) {
        Showtime showtime =  showtimeRepository.findByShowtimeId(showtimeId);
        if(showtime == null){
            throw new NotFoundException();
        }
        return mapToDTO(showtime, new ShowtimeDTO());
    }

    public Integer create(final ShowtimeDTO showtimeDTO) {
        log.info("Creating showtimes with values: movieId={}, theaterId={}, date={}, timeSlot={}, price={}",
                showtimeDTO.getMovie(),
                showtimeDTO.getTheater(),
                showtimeDTO.getShowDate(),
                showtimeDTO.getTimeslotIds(),
                showtimeDTO.getPrice());

        final Showtime showtime = new Showtime();
        mapToEntity(showtimeDTO, showtime);
        return showtimeRepository.save(showtime).getShowtimeId();
    }


    public void update(final Integer showtimeId, final ShowtimeDTO showtimeDTO) {
        final Showtime showtime = showtimeRepository.findByShowtimeId(showtimeId);
        if(showtime == null){
            throw new NotFoundException();
        }
        mapToEntity(showtimeDTO, showtime);
        showtimeRepository.save(showtime);
    }

    public void delete(final Integer showtimeId) {
        showtimeRepository.deleteByShowtimeId(showtimeId);
    }

    private ShowtimeDTO mapToDTO(final Showtime showtime, final ShowtimeDTO showtimeDTO) {
        showtimeDTO.setId(showtime.getId());
        showtimeDTO.setShowtimeId(showtime.getShowtimeId());
        showtimeDTO.setShowDate(showtime.getShowDate());
        showtimeDTO.setPrice(showtime.getPrice());
        showtimeDTO.setMovie(showtime.getMovie() != null ? showtime.getMovie().getMovieId() : null);
        showtimeDTO.setTheater(showtime.getTheater() != null ? showtime.getTheater().getTheaterId() : null);
        if (showtime.getTimeslotIds() != null) {
            List<Integer> timeslotIds = new ArrayList<>();
            for (TimeSlot timeSlot : showtime.getTimeslotIds()) {
                if (timeSlot != null) {
                    timeslotIds.add(timeSlot.getTimeSlotId());
                }
            }
            showtimeDTO.setTimeslotIds(timeslotIds);
        } else {
            showtimeDTO.setTimeslotIds(null);
        }
        return showtimeDTO;
    }

    private Showtime mapToEntity(final ShowtimeDTO showtimeDTO, final Showtime showtime) {
        showtime.setId(showtimeDTO.getId());
        showtime.setShowDate(showtimeDTO.getShowDate());
        showtime.setPrice(showtimeDTO.getPrice());
        log.info("Setting movieId {}", showtimeDTO.getMovie());
        final Movie movie = showtimeDTO.getMovie() == null ? null : movieRepository.findByMovieId(showtimeDTO.getMovie());
        if(movie == null){
            log.info("movie is null");
            throw new NotFoundException();
        }
        showtime.setMovie(movie);
        List<TimeSlot> timeSlots = new ArrayList<>();
        if (showtimeDTO.getTimeslotIds() == null || showtimeDTO.getTimeslotIds().isEmpty()) {
            log.info("timeSlots list is empty");
            throw new NotFoundException();
        }
        for (Integer timeslotId : showtimeDTO.getTimeslotIds()) {
            TimeSlot timeSlot = timeSlotRepository.findByTimeSlotId(timeslotId);
            if (timeSlot == null) {
                log.info("timeSlot with id {} is null", timeslotId);
                throw new NotFoundException();
            }

            timeSlots.add(timeSlot);
        }
        showtime.setTimeslotIds(timeSlots);
        final Theater theater = showtimeDTO.getTheater() == null ? null : theaterRepository.findByTheaterId(showtimeDTO.getTheater());
        if (theater == null) {
            log.info("theater is null");
            throw new NotFoundException();
        }
        showtime.setTheater(theater);
        return showtime;
    }

    public ReferencedWarning getReferencedWarning(final Integer showtimeId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Showtime showtime = showtimeRepository.findByShowtimeId(showtimeId);
        if(showtime == null){
            throw new NotFoundException();
        }
        final Booking showtimeBooking = bookingRepository.findFirstByShowtime(showtime);
        if (showtimeBooking != null) {
            referencedWarning.setKey("showtime.booking.showtime.referenced");
            referencedWarning.addParam(showtimeBooking.getBookingId());
            return referencedWarning;
        }
        return null;
    }

}
