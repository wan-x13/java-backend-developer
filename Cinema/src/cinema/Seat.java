package Cinema.src.cinema;


import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    private int row ;
    private int column;
    private  int price;

    public Seat(int row, int column){
        this.row = row;
        this.column = column;
        this.price = row <= 4 ? 10 : 8;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Seat seat)) {
            return false;
        }
        return this.row == seat.getRow() && this.column == seat.getColumn() && this.price == seat.getPrice();
    }



}
