package movie.ticket.movie_ticket_booking.service;

import lombok.extern.slf4j.Slf4j;
import movie.ticket.movie_ticket_booking.entity.*;
import movie.ticket.movie_ticket_booking.modelDTO.ShowtimeDTO;
import movie.ticket.movie_ticket_booking.repository.*;
import movie.ticket.movie_ticket_booking.util.NotFoundException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        Showtime showtime = showtimeRepository.findByShowtimeId(showtimeId);
        if (showtime == null) {
            throw new NotFoundException();
        }
        return mapToDTO(showtime, new ShowtimeDTO());
    }

    public Integer create(final ShowtimeDTO showtimeDTO) {
        log.info("Creating showtimes with values: movieId={}, theaterId={}, date={}, timeSlot={}, price={}",
                showtimeDTO.getMovie(),
                showtimeDTO.getTheater(),
                showtimeDTO.getShowDate(),
                showtimeDTO.getTimeSlotIds(),
                showtimeDTO.getPrice());

        final Showtime showtime = new Showtime();
        mapToEntity(showtimeDTO, showtime);
        log.info("After Creating showtimes with values: movieId={}, theaterId={}, date={}, timeSlot={}, price={}",
                showtime.getMovie(),
                showtime.getTheater(),
                showtime.getShowDate(),
                showtime.getTimeslotIds(),
                showtime.getPrice());

        return showtimeRepository.save(showtime).getShowtimeId();
    }


    public void update(final Integer showtimeId, final ShowtimeDTO showtimeDTO) {
        final Showtime showtime = showtimeRepository.findByShowtimeId(showtimeId);
        if (showtime == null) {
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
        showtimeDTO.setMovie(showtime.getMovie().getMovieId());
        showtimeDTO.setTheater(showtime.getTheater().getTheaterId());
        showtimeDTO.setAvailableSeats(showtime.getAvailableSeats());

        // Map the TimeSlot objects to their IDs
        List<Integer> timeSlotIds = showtime.getTimeslotIds().stream()
                .map(TimeSlot::getTimeSlotId) // Assuming TimeSlot has a getTimeSlotId() method
                .collect(Collectors.toList());
        showtimeDTO.setTimeSlotIds(timeSlotIds); // Set the list of IDs in the DTO

        return showtimeDTO;
    }


    private Showtime mapToEntity(final ShowtimeDTO showtimeDTO, final Showtime showtime) {
        showtime.setId(showtimeDTO.getId());
        showtime.setShowDate(showtimeDTO.getShowDate());
        showtime.setPrice(showtimeDTO.getPrice());
        showtime.setAvailableSeats(showtimeDTO.getAvailableSeats());

        // Fetch Movie and Theater
        final Movie movie = movieRepository.findByMovieId(showtimeDTO.getMovie());
        if (movie == null) {
            throw new NotFoundException();
        }
        showtime.setMovie(movie);

        final Theater theater = theaterRepository.findByTheaterId(showtimeDTO.getTheater());
        if (theater == null) {
            throw new NotFoundException();
        }
        showtime.setTheater(theater);

        // Fetch TimeSlot objects using the IDs and set them in the Showtime entity
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByTimeSlotIdIn(showtimeDTO.getTimeSlotIds());
        log.info("Fetched {} TimeSlot(s) for showtime of {}", timeSlots.size(), showtimeDTO.getTimeSlotIds());
        showtime.setTimeslotIds(timeSlots);  // Set the fetched TimeSlots

        return showtime;
    }

    public ReferencedWarning getReferencedWarning(final Integer showtimeId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Showtime showtime = showtimeRepository.findByShowtimeId(showtimeId);
        if (showtime == null) {
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

    public List<ShowtimeDTO> getShowtimeByDate(Date showDate) {
        log.info("Fetching showtimes for date: {}", showDate);
        final List<Showtime> showtimes = showtimeRepository.findAllByShowDate(showDate);
        if (showtimes == null) {
            log.info("No showtimes found for date: {}", showDate);
            throw new NotFoundException();
        }
        log.info("Found {} showtimes for date: {}", showtimes.size(), showDate);
        return showtimes.stream()
                .map(showtime -> mapToDTO(showtime, new ShowtimeDTO()))
                .toList();
    }
}
