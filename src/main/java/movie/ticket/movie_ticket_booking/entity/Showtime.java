package movie.ticket.movie_ticket_booking.entity;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;


@Document
@Getter
@Setter
public class Showtime {

    @Id
    private String id;

    private Integer showtimeId;

    @NotNull
    private Date showDate;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @Field(
            targetType = FieldType.DECIMAL128
    )
    private BigDecimal price;

    private String availableSeats;

    @DocumentReference(lazy = true)
    private Movie movie;

    @DocumentReference(lazy = true)
    private Theater theater;

    @DocumentReference(lazy = true)
    private TimeSlot timeSlot;

    @DocumentReference(lazy = true, lookup = "{ 'showtime' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Booking> showtimeBookings;

}
