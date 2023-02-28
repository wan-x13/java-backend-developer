package banking.Card;

public class Card {
    private final long AccountIdentifier ;
    private  int pin;
    public Card(long accountIdentifier , int pin){
        this.AccountIdentifier = accountIdentifier;
        this.pin = pin;
    }
    public int getPin() {
        return pin;
    }
    public long getAccountIdentifier() {
        return AccountIdentifier;
    }
}
