package movie.ticket.movie_ticket_booking.entity;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
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
public class Showtime {

    @Id
    private Integer showtimeId;

    @NotNull
    private String showtimeTime;

    @NotNull
    private Date showDate;

    @NotNull
    private List<String> seatSelected;

    @NotNull
    private String seatType;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @Field(
            targetType = FieldType.DECIMAL128
    )
    private BigDecimal price;

    @DocumentReference(lazy = true)
    private Movie movie;

    @DocumentReference(lazy = true)
    private Theater theater;

    @DocumentReference(lazy = true, lookup = "{ 'showtime' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Booking> showtimeBookings;

}
