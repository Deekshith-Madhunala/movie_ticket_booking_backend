package movie.ticket.movie_ticket_booking.entity;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
public class Booking {

    @Id
    private Integer bookingId;

    @Size(max = 255)
    private String paymentStatus;

    @Size(max = 255)
    private String bookingStatus;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @Field(
            targetType = FieldType.DECIMAL128
    )
    private BigDecimal totalAmount;

    private OffsetDateTime createdAt;

    private OffsetDateTime cancelledAt;

    @DocumentReference(lazy = true)
    private User user;

    @DocumentReference(lazy = true)
    private Showtime showtime;

    @DocumentReference(lazy = true, lookup = "{ 'booking' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Payment> bookingPayments;

}
