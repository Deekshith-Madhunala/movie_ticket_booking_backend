package movie.ticket.movie_ticket_booking.service;

import java.util.List;
import movie.ticket.movie_ticket_booking.entity.Booking;
import movie.ticket.movie_ticket_booking.entity.User;
import movie.ticket.movie_ticket_booking.modelDTO.UserDTO;
import movie.ticket.movie_ticket_booking.repository.BookingRepository;
import movie.ticket.movie_ticket_booking.repository.UserRepository;
import movie.ticket.movie_ticket_booking.util.NotFoundException;
import movie.ticket.movie_ticket_booking.util.PasswordEncryptionUtil;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public UserService(final UserRepository userRepository,
            final BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer userId) {
        User user = userRepository.findByUserId(userId);
        if(user == null){
            throw new NotFoundException();
        }
        return mapToDTO(user, new UserDTO());
    }

    public User create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        String encryptedPassword = PasswordEncryptionUtil.encryptPassword(userDTO.getPassword());
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }


    public void update(final Integer userId, final UserDTO userDTO) {
        final User user = userRepository.findByUserId(userId);
        if(user == null){
            throw new NotFoundException();
        }
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Integer userId) {
        userRepository.deleteByUserId(userId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUserId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setCity(user.getCity());
        userDTO.setZipCode(user.getZipCode());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setCreatedAt(user.getCreatedAt());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setCity(userDTO.getCity());
        user.setZipCode(userDTO.getZipCode());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setCreatedAt(userDTO.getCreatedAt());
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Integer userId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findByUserId(userId);
        if(user == null){
            throw new NotFoundException();
        }
        final Booking userBooking = bookingRepository.findFirstByUser(user);
        if (userBooking != null) {
            referencedWarning.setKey("user.booking.user.referenced");
            referencedWarning.addParam(userBooking.getBookingId());
            return referencedWarning;
        }
        return null;
    }

    public UserDTO findByEmailAndPassword(String email, String password) {
        final User user = userRepository.findByEmail(email); // Find user by email
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (!PasswordEncryptionUtil.matchPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return mapToDTO(user, new UserDTO());
    }

}
