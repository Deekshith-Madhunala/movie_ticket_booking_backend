package movie.ticket.movie_ticket_booking.service;

import movie.ticket.movie_ticket_booking.entity.Booking;
import movie.ticket.movie_ticket_booking.entity.Payment;
import movie.ticket.movie_ticket_booking.entity.Showtime;
import movie.ticket.movie_ticket_booking.entity.User;
import movie.ticket.movie_ticket_booking.modelDTO.BookingDTO;
import movie.ticket.movie_ticket_booking.repository.BookingRepository;
import movie.ticket.movie_ticket_booking.repository.PaymentRepository;
import movie.ticket.movie_ticket_booking.repository.ShowtimeRepository;
import movie.ticket.movie_ticket_booking.repository.UserRepository;
import movie.ticket.movie_ticket_booking.util.NotFoundException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
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
        return bookings.stream().map(booking -> mapToDTO(booking, new BookingDTO())).toList();
    }

    public BookingDTO get(final Integer bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking == null) {
            throw new NotFoundException();
        }
        return mapToDTO(booking, new BookingDTO());
    }


    public Integer create(final BookingDTO bookingDTO) {
        final Booking booking = new Booking();
        mapToEntity(bookingDTO, booking);
        return bookingRepository.save(booking).getBookingId();
    }

    public void update(final Integer bookingId, final BookingDTO bookingDTO) {
        final Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking == null) {
            throw new NotFoundException();
        }
        mapToEntity(bookingDTO, booking);
        bookingRepository.save(booking);
    }

    public void delete(final Integer bookingId) {
        bookingRepository.deleteByBookingId(bookingId);
    }

    private BookingDTO mapToDTO(final Booking booking, final BookingDTO bookingDTO) {
        bookingDTO.setId(booking.getId());
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
        booking.setBookingId(bookingDTO.getBookingId());
        booking.setPaymentStatus(bookingDTO.getPaymentStatus());
        booking.setBookingStatus(bookingDTO.getBookingStatus());
        booking.setTotalAmount(bookingDTO.getTotalAmount());
        booking.setCreatedAt(bookingDTO.getCreatedAt());
        booking.setCancelledAt(bookingDTO.getCancelledAt());
        final User user = bookingDTO.getUser() == null ? null : userRepository.findByUserId(bookingDTO.getUser());
        if (user == null) {
            throw new NotFoundException();
        }
        booking.setUser(user);
        final Showtime showtime = bookingDTO.getShowtime() == null ? null : showtimeRepository.findByShowtimeId(bookingDTO.getShowtime());
        if (showtime == null) {
            throw new NotFoundException();
        }
        booking.setShowtime(showtime);
        return booking;
    }

    public ReferencedWarning getReferencedWarning(final Integer bookingId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking == null) {
            throw new NotFoundException();
        }
        final Payment bookingPayment = paymentRepository.findFirstByBooking(booking);
        if (bookingPayment != null) {
            referencedWarning.setKey("booking.payment.booking.referenced");
            referencedWarning.addParam(bookingPayment.getPaymentId());
            return referencedWarning;
        }
        return null;
    }

    public List<Document> getBookingDetailsByUserId(String userId) {
        // Convert userId to ObjectId
        ObjectId userObjectId = new ObjectId(userId);

        // Lookup operation to join with showtime collection
        LookupOperation lookupShowtime = LookupOperation.newLookup().from("showtime") // the collection name
                .localField("showtime") // field in booking collection
                .foreignField("_id") // field in showtime collection
                .as("showtimeDetails");

        // Lookup operation to join with movie collection
        LookupOperation lookupMovie = LookupOperation.newLookup().from("movie") // the collection name
                .localField("showtimeDetails.movie") // field in showtime collection
                .foreignField("_id") // field in movie collection
                .as("movieDetails");

        // Lookup operation to join with theater collection
        LookupOperation lookupTheater = LookupOperation.newLookup().from("theater") // the collection name
                .localField("showtimeDetails.theater") // field in showtime collection
                .foreignField("_id") // field in theater collection
                .as("theaterDetails");

        // Aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("user").is(userObjectId)), // Match on user _id
                lookupShowtime, lookupMovie, lookupTheater, Aggregation.unwind("showtimeDetails"), // Unwind to get single showtime
                Aggregation.unwind("movieDetails"), // Unwind to get single movie
                Aggregation.unwind("theaterDetails") // Unwind to get single theater
        );

        // Execute aggregation
        return mongoTemplate.aggregate(aggregation, "booking", Document.class).getMappedResults();
    }
}
