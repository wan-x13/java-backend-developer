package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.Driver;

public class DBconnection {
    public static String dbName;
    private static final String driver = "org.h2.Driver";
    public static  Statement statement ;
    public  static  Connection connection;

    public static void createConnection(){
        try{
            Class.forName(driver);
            connection = DriverManager.getConnection("jdbc:h2:file:../task/src/carsharing/db/"+dbName);
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


}
