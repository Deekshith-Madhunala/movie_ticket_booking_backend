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


@Getter
@Setter
public class ShowtimeDTO {

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "75.08")
    private BigDecimal price;

    private Integer movie;

    private Integer theater;

}
