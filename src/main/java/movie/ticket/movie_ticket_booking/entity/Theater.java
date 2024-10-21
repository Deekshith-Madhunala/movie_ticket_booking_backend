package movie.ticket.movie_ticket_booking.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
@Getter
@Setter
public class Theater {

    @Id
    private Integer theaterId;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 255)
    private String address;

    @NotNull
    private Integer totalSeats;

    @DocumentReference(lazy = true, lookup = "{ 'theater' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Showtime> theaterShowtimes;

}
