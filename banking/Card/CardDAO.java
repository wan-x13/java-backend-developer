package banking.Card;

import banking.Database.CardDB;

import java.util.*;

public class CardDAO implements  CardSystem{
    private static final Random random = new Random();
    private static List<Card> accounts;
    static Map<Long, Integer> accountCache = new HashMap<>();
    public  CardDAO(){
        accounts = new ArrayList<>();
    }
    @Override
    public List<Card> getAllAccounts() {

        return null;
    }
    @Override
    public void createAccount() {
          long accountIdentifier;
          int pin ;
          long creditCard;
           while (true){
               accountIdentifier = generateNumber(9);
               if(!accountCache.containsKey(accountIdentifier)){
                   pin = (int) generateNumber(4);
                   int bin = 400000;
                   String temp = bin +""+accountIdentifier;
                   creditCard = Long.parseLong(temp);
                   if(luhnAlgorithm(creditCard)){
                       CardDB.addCard(creditCard,pin);
                       accounts.add(new Card(creditCard, pin));
                       accountCache.put(accountIdentifier, pin);
                       break;
                   }
               }
           }

           System.out.printf("""
                   Your card has been created
                   Your card number:
                   %d
                   Your card PIN:
                   %s
                   """,creditCard, pin );

    }
    public static long generateNumber( int range) {
        StringBuilder sb = new StringBuilder();
        int temp = range  == 9 ? 9 : 3;
        sb.append(random.nextInt(9)+1);
        for (int i = 0; i < temp; i++) {
            sb.append(random.nextInt(10));
        }
        return Long.parseLong(sb.toString());
    }
    public static boolean ifAccountsExist(long accountNumber , int pin){
        return CardDB.findIfExistsCard(accountNumber ,pin);
    }
    public static boolean luhnAlgorithm(long accountNumber){
        var list = new ArrayList<>(Arrays.stream(String.valueOf(accountNumber).split(""))
                .map(Integer::parseInt)
                .toList());
        int lastElement = list.get(list.size()-1);
        list.remove(list.size()-1);
        int sum = 0;
        for(int i = list.size()-1; i >= 0; i--){
            int temp = i%2 == 0 ? list.get(i)*2 : list.get(i);
            sum += temp > 9 ? temp -9 : temp;
        }
        return  (sum + lastElement) % 10 == 0;
    }
}
