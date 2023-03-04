package Cinema.src.cinema;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private int total_rows = 9;
    private int total_columns = 9;
    private List<Seat> available_seats = new ArrayList<>();

    {
        for(int i = 1 ; i <= total_rows; i++){
            for( int j= 1; j<= total_columns; j++){
                available_seats.add(new Seat(i , j));
            }
        }
    }
    public List<Seat> getAvailable_seats() {
        return available_seats;
    }
    public void removeSeat(Seat seat){
        available_seats.remove(seat);
    }
    public boolean ifSeatExits(Seat seat) {
        boolean result = false;
        for (Seat seat1 : available_seats) {
            if (seat1.getRow() == seat.getRow()) {
                if (seat1.getColumn() == seat.getColumn()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    public Optional<Seat> findSeat(Seat seat){
        return available_seats.stream().
                filter(it->it.getRow() == seat.getRow() && it.getColumn() == seat.getColumn())
                .findFirst();
    }
}
