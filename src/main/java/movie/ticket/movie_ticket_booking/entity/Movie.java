package movie.ticket.movie_ticket_booking.entity;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;


@Document
@Getter
@Setter
public class Movie {

    @Id
    private Integer movieId;

    @NotNull
    @Size(max = 255)
    private String title;

    private String description;

    private String poster;

    @NotNull
    private Integer duration;

    @Size(max = 100)
    private String genre;

    private LocalDate releaseDate;

    @Digits(integer = 3, fraction = 1)
    @Field(
            targetType = FieldType.DECIMAL128
    )
    private BigDecimal rating;

    @DocumentReference(lazy = true, lookup = "{ 'movie' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Showtime> movieShowtimes;

}
