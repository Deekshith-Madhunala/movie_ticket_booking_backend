package movie.ticket.movie_ticket_booking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;


@Document
@Getter
@Setter
public class TimeSlot {

    @Id
    private String id;

    private Integer timeSlotId;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeSlot;
}
