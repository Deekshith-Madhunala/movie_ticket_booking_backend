package movie.ticket.movie_ticket_booking.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Getter
@Setter
public class PrimarySequence {

    @Id
    private String id;

    private String sequence_id;

    private long seq;

}
