package movie.ticket.movie_ticket_booking.service;

import java.util.List;
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
        return showtimeRepository.findById(showtimeId)
                .map(showtime -> mapToDTO(showtime, new ShowtimeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ShowtimeDTO showtimeDTO) {
        final Showtime showtime = new Showtime();
        mapToEntity(showtimeDTO, showtime);
        return showtimeRepository.save(showtime).getShowtimeId();
    }

    public void update(final Integer showtimeId, final ShowtimeDTO showtimeDTO) {
        final Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(showtimeDTO, showtime);
        showtimeRepository.save(showtime);
    }

    public void delete(final Integer showtimeId) {
        showtimeRepository.deleteById(showtimeId);
    }

    private ShowtimeDTO mapToDTO(final Showtime showtime, final ShowtimeDTO showtimeDTO) {
        showtimeDTO.setShowtimeId(showtime.getShowtimeId());
        showtimeDTO.setShowtimeTime(showtime.getShowtimeTime());
        showtimeDTO.setShowDate(showtime.getShowDate());
        showtimeDTO.setSeatType(showtime.getSeatType());
        showtimeDTO.setSeatSelected(showtime.getSeatSelected());
        showtimeDTO.setPrice(showtime.getPrice());
        showtimeDTO.setPrice(showtime.getPrice());
        showtimeDTO.setMovie(showtime.getMovie() == null ? null : showtime.getMovie().getMovieId());
        showtimeDTO.setTheater(showtime.getTheater() == null ? null : showtime.getTheater().getTheaterId());

        return showtimeDTO;
    }

    private Showtime mapToEntity(final ShowtimeDTO showtimeDTO, final Showtime showtime) {
        showtime.setShowtimeTime(showtimeDTO.getShowtimeTime());
        showtime.setShowDate(showtimeDTO.getShowDate());
        showtime.setSeatType(showtimeDTO.getSeatType());
        showtime.setSeatSelected(showtimeDTO.getSeatSelected());
        showtime.setPrice(showtimeDTO.getPrice());
        final Movie movie = showtimeDTO.getMovie() == null ? null : movieRepository.findById(showtimeDTO.getMovie())
                .orElseThrow(() -> new NotFoundException("movie not found"));
        showtime.setMovie(movie);
        final Theater theater = showtimeDTO.getTheater() == null ? null : theaterRepository.findById(showtimeDTO.getTheater())
                .orElseThrow(() -> new NotFoundException("theater not found"));
        showtime.setTheater(theater);
        return showtime;
    }

    public ReferencedWarning getReferencedWarning(final Integer showtimeId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(NotFoundException::new);
        final Booking showtimeBooking = bookingRepository.findFirstByShowtime(showtime);
        if (showtimeBooking != null) {
            referencedWarning.setKey("showtime.booking.showtime.referenced");
            referencedWarning.addParam(showtimeBooking.getBookingId());
            return referencedWarning;
        }
        return null;
    }

}
