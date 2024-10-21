package movie.ticket.movie_ticket_booking.modelDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TheaterDTO {

    private Integer theaterId;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 255)
    private String address;

    @NotNull
    private Integer totalSeats;

}
