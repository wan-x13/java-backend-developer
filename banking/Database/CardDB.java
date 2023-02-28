package banking.Database;

import java.sql.*;

public class CardDB {
    private static String urlSource;
    public static  Statement statement ;
    public  static  Connection connection;
    public static void createConnection(String fileName){
        String url = "jdbc:sqlite:"+fileName;
        try{
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            connection.setAutoCommit(true);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void closeConnection() throws SQLException {
        statement.close();
        connection.close();
    }

    public static void createCardTable(){
        String req = """
                CREATE TABLE IF NOT EXISTS card(
                id INTEGER PRIMARY KEY,
                number TEXT NOT NULL,
                pin TEXT NOT NULL,
                balance INTEGER DEFAULT 0);
                """;
        try{
            statement.executeUpdate(req);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static void addCard(long accountIdentifier , int pin){
        String req = String.format("""
                INSERT INTO card(number, pin)
                VALUES('%s','%s');
                """, accountIdentifier, pin);
        try{
            statement.executeUpdate(req);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static boolean findIfExistsCard(long accountNumber, int pin){
        String req = String.format("""
                SELECT number,pin 
                FROM card
                WHERE number='%s' AND pin ='%s' ;,
                """, accountNumber, pin);
        boolean isTrue = true;
        try {
            PreparedStatement statement = connection.prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()){
                isTrue = false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return  isTrue;
    }
    public static int getBalance(long cardNumber){
        String req = """
                SELECT balance FROM card
                WHERE number =  ?;
                """;
        int balance = 0;
        try {
            PreparedStatement statement1 = connection.prepareStatement(req);
            statement1.setString(1, String.valueOf(cardNumber));
            ResultSet resultSet = statement1.executeQuery();
            if(resultSet.next()){
                balance = resultSet.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return  balance;
    }
    public static void addIncome(int amount  , long cardNumber){

       String req = """
               UPDATE card
               SET balance = balance + ?
               WHERE number = ?;
               """;
       try {
           PreparedStatement statement1 = connection.prepareStatement(req);
           statement1.setInt(1, amount);
           statement1.setString(2, String.valueOf(cardNumber));
           statement1.executeUpdate();
           System.out.println("Income was added!");
       }catch (SQLException e){
           if(connection != null){
               try {
                   connection.rollback();
               }catch (SQLException except){
                   except.printStackTrace();
               }
           }
       }
    }
    public static void transferAmount (long cardNumberFrom , int amount , long cardNumberTo)  {
        int balance = getBalance(cardNumberFrom);
        String updateUSer1 = String.format("""
                UPDATE card
                SET balance = balance - %d
                WHERE number = '%s';
                """, amount, cardNumberFrom);
        String updateUser2 = String.format("""
                UPDATE card
                SET balance  = balance + %d
                WHERE number = '%s';
                """,amount,cardNumberTo);
        if( balance >= amount){
            try {
                connection.setAutoCommit(false);
                PreparedStatement statement1 = connection.prepareStatement(updateUSer1);
                statement1.executeUpdate();
                    Savepoint savepoint = connection.setSavepoint();
                    PreparedStatement statement2 = connection.prepareStatement(updateUser2);
                    statement2.executeUpdate();
                    connection.commit();
                    System.out.println("Success!");

            }catch (SQLException e){
                if(connection != null){
                    try {
                        connection.rollback();
                    }catch (SQLException exception){
                        System.out.println("here second");
                        exception.printStackTrace();
                    }
                }
            }finally {
                try {
                    assert connection != null;
                    connection.setAutoCommit(true);
                }catch (SQLException except){
                    except.printStackTrace();
                }
            }
        }else{
            System.out.println("Not enough money!");
        }

    }
    public static boolean findIfExistsCardForDestination(long accountNumberDestination){
        String req = String.format("""
                SELECT number
                FROM card
                WHERE number='%s';
                """, accountNumberDestination);
        boolean isTrue = true;
        try {
            PreparedStatement statement = connection.prepareStatement(req);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()){
                isTrue = false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return  isTrue;
    }

    public static void closeAccount(long cardNumber){
        String req = """
                DELETE FROM card
                WHERE number = ?;
                """;
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement1 = connection.prepareStatement(req);
            statement1.setString(1, String.valueOf(cardNumber));
            statement1.executeUpdate();
            connection.commit();
            System.out.println("The account has been closed!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
            assert  connection != null;
            try {
                connection.rollback();
            }catch (SQLException except){
                except.printStackTrace();
            }
        }finally {
            try {
                assert connection != null;
                connection.setAutoCommit(true);
            }catch (SQLException exception){
                exception.printStackTrace();
            }
        }
    }

}
