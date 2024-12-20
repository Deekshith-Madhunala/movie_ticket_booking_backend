package movie.ticket.movie_ticket_booking.modelDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;


@Getter
@Setter
public class TimeSlotDTO {

    @Id
    private String id;

    private Integer timeSlotId;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeSlot;
}
