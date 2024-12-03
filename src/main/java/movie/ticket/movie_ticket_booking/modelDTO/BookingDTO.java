package movie.ticket.movie_ticket_booking.modelDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookingDTO {

    private String id;

    private Integer bookingId;

    @Size(max = 255)
    private String paymentStatus;

    @Size(max = 255)
    private String bookingStatus;

    @NotNull
    private List<String> seatSelected;

    @NotNull
    private String seatType;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "52.08")
    private BigDecimal totalAmount;

    private OffsetDateTime createdAt;

    private OffsetDateTime cancelledAt;

    private Integer user;

    private Integer showtime;

    private OffsetDateTime bookingDateAndTime;

}
