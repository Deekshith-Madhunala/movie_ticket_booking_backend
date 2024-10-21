package movie.ticket.movie_ticket_booking.modelDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MovieDTO {

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "67.8")
    private BigDecimal rating;
    
}
