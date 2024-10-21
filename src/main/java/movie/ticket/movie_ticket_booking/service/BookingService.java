package movie.ticket.movie_ticket_booking.service;

import movie.ticket.movie_ticket_booking.entity.Booking;
import movie.ticket.movie_ticket_booking.entity.Payment;
import movie.ticket.movie_ticket_booking.entity.Showtime;
import movie.ticket.movie_ticket_booking.entity.User;
import movie.ticket.movie_ticket_booking.modelDTO.BookingDTO;
import movie.ticket.movie_ticket_booking.modelDTO.BookingDetailsDTO;
import movie.ticket.movie_ticket_booking.repository.BookingRepository;
import movie.ticket.movie_ticket_booking.repository.PaymentRepository;
import movie.ticket.movie_ticket_booking.repository.ShowtimeRepository;
import movie.ticket.movie_ticket_booking.repository.UserRepository;
import movie.ticket.movie_ticket_booking.util.NotFoundException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookingService {


    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, ShowtimeRepository showtimeRepository, PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.showtimeRepository = showtimeRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<BookingDTO> findAll() {
        final List<Booking> bookings = bookingRepository.findAll(Sort.by("bookingId"));
        return bookings.stream()
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .toList();
    }

    public BookingDTO get(final Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final BookingDTO bookingDTO) {
        final Booking booking = new Booking();
        mapToEntity(bookingDTO, booking);
        return bookingRepository.save(booking).getBookingId();
    }

    public void update(final Integer bookingId, final BookingDTO bookingDTO) {
        final Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookingDTO, booking);
        bookingRepository.save(booking);
    }

    public void delete(final Integer bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    private BookingDTO mapToDTO(final Booking booking, final BookingDTO bookingDTO) {
        bookingDTO.setBookingId(booking.getBookingId());
        bookingDTO.setPaymentStatus(booking.getPaymentStatus());
        bookingDTO.setBookingStatus(booking.getBookingStatus());
        bookingDTO.setTotalAmount(booking.getTotalAmount());
        bookingDTO.setCreatedAt(booking.getCreatedAt());
        bookingDTO.setCancelledAt(booking.getCancelledAt());
        bookingDTO.setUser(booking.getUser() == null ? null : booking.getUser().getUserId());
        bookingDTO.setShowtime(booking.getShowtime() == null ? null : booking.getShowtime().getShowtimeId());
        return bookingDTO;
    }

    private Booking mapToEntity(final BookingDTO bookingDTO, final Booking booking) {
        booking.setPaymentStatus(bookingDTO.getPaymentStatus());
        booking.setBookingStatus(bookingDTO.getBookingStatus());
        booking.setTotalAmount(bookingDTO.getTotalAmount());
        booking.setCreatedAt(bookingDTO.getCreatedAt());
        booking.setCancelledAt(bookingDTO.getCancelledAt());
        final User user = bookingDTO.getUser() == null ? null : userRepository.findById(bookingDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        booking.setUser(user);
        final Showtime showtime = bookingDTO.getShowtime() == null ? null : showtimeRepository.findById(bookingDTO.getShowtime())
                .orElseThrow(() -> new NotFoundException("showtime not found"));
        booking.setShowtime(showtime);
        return booking;
    }

    public ReferencedWarning getReferencedWarning(final Integer bookingId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(NotFoundException::new);
        final Payment bookingPayment = paymentRepository.findFirstByBooking(booking);
        if (bookingPayment != null) {
            referencedWarning.setKey("booking.payment.booking.referenced");
            referencedWarning.addParam(bookingPayment.getPaymentId());
            return referencedWarning;
        }
        return null;
    }

    public List<BookingDetailsDTO> getBookingDetailsByUserId(Integer userId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("user").is(userId)),
                Aggregation.lookup("showtime", "showtime", "_id", "showtimeDetails"),
                Aggregation.unwind("showtimeDetails", true),
                Aggregation.lookup("movie", "showtimeDetails.movie", "_id", "movieDetails"),
                Aggregation.unwind("movieDetails", true),
                Aggregation.lookup("theater", "showtimeDetails.theater", "_id", "theaterDetails"),
                Aggregation.unwind("theaterDetails", true),
                Aggregation.project("paymentStatus", "bookingStatus", "totalAmount", "createdAt", "cancelledAt")
                        .and("showtimeDetails._id").as("showtimeId") // Add this line
                        .and("showtimeDetails").as("showtime")
                        .and("movieDetails._id").as("movieId") // Add this line
                        .and("movieDetails").as("movie")
                        .and("theaterDetails._id").as("theaterId") // Add this line
                        .and("theaterDetails").as("theater")
        );

        AggregationResults<BookingDetailsDTO> results = mongoTemplate.aggregate(aggregation, "booking", BookingDetailsDTO.class);
        return results.getMappedResults();
    }
}
