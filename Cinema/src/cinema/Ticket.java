package Cinema.src.cinema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
public class Ticket {
    Seat ticket;
    public Ticket(Seat seat ){
        this.ticket = seat;

    }


}
