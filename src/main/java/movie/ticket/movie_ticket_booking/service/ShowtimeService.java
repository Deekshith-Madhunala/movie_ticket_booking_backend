package movie.ticket.movie_ticket_booking.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import movie.ticket.movie_ticket_booking.entity.*;
import movie.ticket.movie_ticket_booking.modelDTO.ShowtimeDTO;
import movie.ticket.movie_ticket_booking.repository.*;
import movie.ticket.movie_ticket_booking.util.NotFoundException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


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
                showtimeDTO.getTimeslotId(),
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
        showtimeDTO.setPrice(showtime.getPrice());
        showtimeDTO.setMovie(showtime.getMovie().getMovieId() == null ? null : showtime.getMovie().getMovieId());
        showtimeDTO.setTheater(showtime.getTheater().getTheaterId() == null ? null : showtime.getTheater().getTheaterId());
        showtimeDTO.setTimeslotId(showtime.getTimeSlot().getTimeSlotId() == null ? null : showtime.getTimeSlot().getTimeSlotId());

        return showtimeDTO;
    }

    private Showtime mapToEntity(final ShowtimeDTO showtimeDTO, final Showtime showtime) {
        showtime.setId(showtimeDTO.getId());
        showtime.setShowDate(showtimeDTO.getShowDate());
        showtime.setPrice(showtimeDTO.getPrice());
        log.info("inside showtime setting {}", showtimeDTO.getMovie());
        log.info("inside showtime setting {}", showtimeDTO.getTheater());
        final Movie movie = showtimeDTO.getMovie() == null ? null : movieRepository.findByMovieId(showtimeDTO.getMovie());

        if(movie == null){
            log.info("movie is null");
            throw new NotFoundException();
        }
        log.info("setting movieId {}", movie.getMovieId());
        showtime.setMovie(movie);
        final TimeSlot timeSlot = showtimeDTO.getTimeslotId() == null ? null : timeSlotRepository.findByTimeSlotId(showtimeDTO.getTimeslotId());

        if(timeSlot == null){
            log.info("timeSlot is null");
            throw new NotFoundException();
        }
        showtime.setTimeSlot(timeSlot);
        final Theater theater = showtimeDTO.getTheater() == null ? null : theaterRepository.findByTheaterId(showtimeDTO.getTheater());
        if(theater == null){
            log.info("theater  is null");
            throw new NotFoundException();
        }        showtime.setTheater(theater);
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
