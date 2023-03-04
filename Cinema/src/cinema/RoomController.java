package Cinema.src.cinema;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@RestController
public class RoomController {

    Room room = new Room();
    Map<String , Ticket> cachePurchase = new ConcurrentHashMap<>();
    UUID uuid;
    Statistics statistics = new Statistics();

    @GetMapping("/seats")
    public Room getRoom(){
        return room;
    }

    @PostMapping("/purchase")
    public ResponseEntity purchaseSeat(@RequestBody Seat seat){
        uuid = UUID.randomUUID();
        String token = uuid.toString();

    if(seat.getRow() < 1 || seat.getRow() > 9 || seat.getColumn() < 1 || seat.getColumn() > 9){
        return ResponseEntity.badRequest().body(
                new ConcurrentHashMap<>(Map.of("error", "The number of a row or a column is out of bounds!"))
        );
    }
    else if(!room.ifSeatExits(seat)){
        return ResponseEntity.badRequest().body(
                new ConcurrentHashMap<>(Map.of(
                        "error",
                        "The ticket has been already purchased!"
                ))
        );
     }
      else {

          Seat seatResponse = null;
          if(room.findSeat(seat).isPresent()){
              seatResponse = room.findSeat(seat).get();
              cachePurchase.put(token , new Ticket(seatResponse));
              statistics.setCurrent_income(seatResponse.getPrice());
              statistics.setNumber_of_available_seats(-1);
              statistics.setNumber_of_purchased_tickets(1);
          }
          room.removeSeat(seatResponse);
        assert seatResponse != null;
        return ResponseEntity.ok(
                new LinkedHashMap<>(
                        Map.of("token", token,
                        "ticket", seatResponse)
                )

        );
      }

    }
    @PostMapping("/return")
    public ResponseEntity returnTicket(@RequestBody Token token){
      if(cachePurchase.containsKey(token.getToken())){
          room.getAvailable_seats().add(cachePurchase.get(token.getToken()).getTicket());
          statistics.setCurrent_income(-cachePurchase.get(token.getToken()).getTicket().getPrice());
          statistics.setNumber_of_available_seats(1);
          statistics.setNumber_of_purchased_tickets(-1);
          return ResponseEntity.ok(
                  Map.of(
                          "returned_ticket",
                          cachePurchase.get(token.getToken()).getTicket()
                  )
          );
      }
      else{
          return ResponseEntity.badRequest().body(
                  Map.of(
                          "error",
                          "Wrong token!"
                  )
          );
      }
    }

    @PostMapping("/stats")
    public ResponseEntity getStats(@RequestParam(required = false) String password){
        System.out.println("here");
        if(Objects.equals(password , "") || !Objects.equals(password,"super_secret")){
            return ResponseEntity.status(401).body(
                    Map.of(
                            "error",
                            "The password is wrong!"
                    )
            );
        }
        else {

            return ResponseEntity.ok(statistics);
        }
    }



}
