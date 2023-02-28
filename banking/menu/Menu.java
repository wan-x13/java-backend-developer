package banking.menu;



import banking.Card.CardDAO;
import banking.Card.CardSystem;
import banking.Database.CardDB;

import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    private static final CardSystem accountDao= new CardDAO();
    private  static final Scanner scanner = new Scanner(System.in);

    private static boolean isOpen = true;
    public static void mainMenu(){
       int input;
       while (isOpen){
           System.out.println("""
               1. Create an account
               2. Log into account
               0. Exit
               """);
           input =scanner.nextInt();
           switch (input){
               case 1 -> createAccount();
               case 2 -> logIntoAccount();
           }
           if(input == 0) {
               break;
           }
           System.out.println();
       };
       System.out.println("Bye!");
        try {
            CardDB.closeConnection();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

   }
   private static void createAccount(){
       accountDao.createAccount();
   }
   private static void logIntoAccount(){

        int  pin;
            System.out.println("Enter your card number:");
            long cardNumber = scanner.nextLong();
            System.out.println("Enter your PIN:");
            pin = scanner.nextInt();
            if(CardDAO.ifAccountsExist(cardNumber,pin)){
                System.out.println("You have successfully logged in!");
                accountBalance(cardNumber);
            }else{
                System.out.println("Wrong card number or PIN!");
               // mainMenu();
            }
   }
   private static void accountBalance(long cardNumberFrom){
        int input;
        boolean openMenu = true;
        while (openMenu){
            System.out.println("""
                1. Balance
                2. Add income
                3. Do transfer
                4. Close account
                5. Log out
                0. Exit
                """);
            input = scanner.nextInt();
            if(input == 0){
                isOpen = false;
                break;
            }
            switch (input){
                case 1 ->{
                    System.out.println();
                    System.out.println("Balance: "+CardDB.getBalance(cardNumberFrom));
                }
                case 2->{
                    System.out.println("Enter income:");
                    int amount = scanner.nextInt();
                    CardDB.addIncome(amount , cardNumberFrom);
                }
                case 3->{
                    System.out.println("""
                            Transfer
                            Enter card number:
                            """);
                    long cardNumberTo = scanner.nextLong();
                    if(!CardDAO.luhnAlgorithm(cardNumberTo)){
                        System.out.println("Probably you made a mistake in the card number. Please try again!");
                    }
                    else if(!CardDB.findIfExistsCardForDestination(cardNumberTo)){
                        System.out.println("Such a card does not exist.");
                    }else{
                        System.out.println("Enter how much money you want to transfer:");
                        int amountToTransfer = scanner.nextInt();
                        assert amountToTransfer != 0;
                        CardDB.transferAmount(cardNumberFrom , amountToTransfer , cardNumberTo);
                    }
                }
                case  4-> {
                    CardDB.closeAccount(cardNumberFrom);
                    openMenu = false;
                }
                case 5-> {

                    System.out.println();
                    System.out.println("You have successfully logged out!");
                    openMenu = false;
                }
            }


        }

   }
}
