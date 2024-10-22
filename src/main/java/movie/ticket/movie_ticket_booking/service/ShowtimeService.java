package movie.ticket.movie_ticket_booking.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import movie.ticket.movie_ticket_booking.entity.Booking;
import movie.ticket.movie_ticket_booking.entity.Movie;
import movie.ticket.movie_ticket_booking.entity.Showtime;
import movie.ticket.movie_ticket_booking.entity.Theater;
import movie.ticket.movie_ticket_booking.modelDTO.ShowtimeDTO;
import movie.ticket.movie_ticket_booking.repository.BookingRepository;
import movie.ticket.movie_ticket_booking.repository.MovieRepository;
import movie.ticket.movie_ticket_booking.repository.ShowtimeRepository;
import movie.ticket.movie_ticket_booking.repository.TheaterRepository;
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

    public ShowtimeService(final ShowtimeRepository showtimeRepository,
            final MovieRepository movieRepository, final TheaterRepository theaterRepository,
            final BookingRepository bookingRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.bookingRepository = bookingRepository;
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
        log.info("Creating ShowtimeDTO using: movieId={}, theaterId={}, seatType={}",
                showtimeDTO.getMovie(),
                showtimeDTO.getTheater(),
//                showtimeDTO.getStartTime(),
                showtimeDTO.getSeatType());
        final Showtime showtime = new Showtime();
        mapToEntity(showtimeDTO, showtime);
        log.info("Creating Show using: showtimeId={}, movie={}, theater={}, seatType={}",
                showtime.getShowtimeId(),
                showtime.getMovie() != null ? showtime.getMovie().getId() : "null", // Replace with appropriate field
                showtime.getTheater() != null ? showtime.getTheater().getId() : "null", // Replace with appropriate field
                showtime.getSeatType());
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
        showtimeDTO.setShowtimeTime(showtime.getShowtimeTime());
        showtimeDTO.setShowDate(showtime.getShowDate());
        showtimeDTO.setSeatType(showtime.getSeatType());
        showtimeDTO.setSeatSelected(showtime.getSeatSelected());
        showtimeDTO.setPrice(showtime.getPrice());
        showtimeDTO.setPrice(showtime.getPrice());
        showtimeDTO.setMovie(showtime.getMovie().getMovieId() == null ? null : showtime.getMovie().getMovieId());
        showtimeDTO.setTheater(showtime.getTheater().getTheaterId() == null ? null : showtime.getTheater().getTheaterId());

        return showtimeDTO;
    }

    private Showtime mapToEntity(final ShowtimeDTO showtimeDTO, final Showtime showtime) {
        showtime.setId(showtimeDTO.getId());
        showtime.setShowtimeTime(showtimeDTO.getShowtimeTime());
        showtime.setShowDate(showtimeDTO.getShowDate());
        showtime.setSeatType(showtimeDTO.getSeatType());
        showtime.setSeatSelected(showtimeDTO.getSeatSelected());
        showtime.setPrice(showtimeDTO.getPrice());
        log.info("inside showtime setting {}", showtimeDTO.getMovie());
        log.info("inside showtime setting {}", showtimeDTO.getTheater());
        final Movie movie = showtimeDTO.getMovie() == null ? null : movieRepository.findByMovieId(showtimeDTO.getMovie());

        if(movie == null){
            throw new NotFoundException();
        }
        log.info("setting movieId {}", movie.getMovieId());
        showtime.setMovie(movie);
        final Theater theater = showtimeDTO.getTheater() == null ? null : theaterRepository.findByTheaterId(showtimeDTO.getTheater());
        if(theater == null){
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
