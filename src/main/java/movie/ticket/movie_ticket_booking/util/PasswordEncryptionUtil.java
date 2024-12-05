package movie.ticket.movie_ticket_booking.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptionUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Encrypt password
    public static String encryptPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    // Check if password matches the encrypted one
    public static boolean matchPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
