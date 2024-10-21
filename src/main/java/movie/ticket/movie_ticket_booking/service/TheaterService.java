package movie.ticket.movie_ticket_booking.service;

import java.util.List;
import movie.ticket.movie_ticket_booking.entity.Showtime;
import movie.ticket.movie_ticket_booking.entity.Theater;
import movie.ticket.movie_ticket_booking.modelDTO.TheaterDTO;
import movie.ticket.movie_ticket_booking.repository.ShowtimeRepository;
import movie.ticket.movie_ticket_booking.repository.TheaterRepository;
import movie.ticket.movie_ticket_booking.util.NotFoundException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final ShowtimeRepository showtimeRepository;

    public TheaterService(final TheaterRepository theaterRepository,
            final ShowtimeRepository showtimeRepository) {
        this.theaterRepository = theaterRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public List<TheaterDTO> findAll() {
        final List<Theater> theaters = theaterRepository.findAll();
        return theaters.stream()
                .map(theater -> mapToDTO(theater, new TheaterDTO()))
                .toList();
    }

    public TheaterDTO get(final Integer theaterId) {
        return theaterRepository.findById(theaterId)
                .map(theater -> mapToDTO(theater, new TheaterDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TheaterDTO theaterDTO) {
        final Theater theater = new Theater();
        mapToEntity(theaterDTO, theater);
        return theaterRepository.save(theater).getTheaterId();
    }

    public void update(final Integer theaterId, final TheaterDTO theaterDTO) {
        final Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(theaterDTO, theater);
        theaterRepository.save(theater);
    }

    public void delete(final Integer theaterId) {
        theaterRepository.deleteById(theaterId);
    }

    private TheaterDTO mapToDTO(final Theater theater, final TheaterDTO theaterDTO) {
        theaterDTO.setTheaterId(theater.getTheaterId());
        theaterDTO.setName(theater.getName());
        theaterDTO.setAddress(theater.getAddress());
        theaterDTO.setTotalSeats(theater.getTotalSeats());
        return theaterDTO;
    }

    private Theater mapToEntity(final TheaterDTO theaterDTO, final Theater theater) {
        theater.setName(theaterDTO.getName());
        theater.setAddress(theaterDTO.getAddress());
        theater.setTotalSeats(theaterDTO.getTotalSeats());
        return theater;
    }

    public ReferencedWarning getReferencedWarning(final Integer theaterId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(NotFoundException::new);
        final Showtime theaterShowtime = showtimeRepository.findFirstByTheater(theater);
        if (theaterShowtime != null) {
            referencedWarning.setKey("theater.showtime.theater.referenced");
            referencedWarning.addParam(theaterShowtime.getShowtimeId());
            return referencedWarning;
        }
        return null;
    }

}
