package movie.ticket.movie_ticket_booking.modelDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private String id;

    private Integer userId;

    @NotNull
    @Size(max = 100)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String city;

    @NotNull
    @Size(max = 255)
    private String zipCode;

    @NotNull
    @Size(max = 255)
    private String dateOfBirth;

    @NotNull
    @Size(max = 100)
    private String lastName;

    @NotNull
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 255)
    private String role;

    @Size(max = 15)
    private String phoneNumber;

    private OffsetDateTime createdAt;

}
