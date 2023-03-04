package Cinema.src.cinema;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {
    private int current_income = 0;
    private int number_of_available_seats = 81;
    private int number_of_purchased_tickets = 0;

    public void setNumber_of_purchased_tickets(int number_of_purchased_tickets) {
        this.number_of_purchased_tickets += number_of_purchased_tickets;
    }
    public void setCurrent_income(int current_income) {
        this.current_income += current_income;
    }
    public void setNumber_of_available_seats(int number_of_available_seats) {
        this.number_of_available_seats += number_of_available_seats;
    }
}
