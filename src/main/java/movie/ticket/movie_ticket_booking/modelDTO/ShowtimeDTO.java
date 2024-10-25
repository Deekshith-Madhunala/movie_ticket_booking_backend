package movie.ticket.movie_ticket_booking.modelDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import movie.ticket.movie_ticket_booking.entity.Theater;
import movie.ticket.movie_ticket_booking.entity.TimeSlot;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Getter
@Setter
public class ShowtimeDTO {

    private String id;

    private Integer showtimeId;

    private Integer timeslotId;

    @NotNull
    private Date showDate;

    private String availableSeats;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "75.08")
    private BigDecimal price;

    private Integer movie;

    private Integer theater;

}
