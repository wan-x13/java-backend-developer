package banking;


import banking.Card.CardDAO;
import banking.Database.CardDB;
import banking.menu.Menu;

import java.util.Random;



public class Main {
    public static void main(String[] args) {
        /**
         * THIS PROJECT USE DATA ACCESS OBJECT PATTERN
         */
        if(args.length> 0 && args[0].equals("-fileName")){
            String fileName = args[1];
            CardDB.createConnection(fileName);
            CardDB.createCardTable();
        }
        Menu.mainMenu();

    }


}