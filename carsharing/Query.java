package carsharing;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Query {
    static Map<String , String> rentedCarCache = new HashMap<>();
    public static void createTableCompany(){

        String createTable = """
               CREATE TABLE IF NOT EXISTS COMPANY(
               ID INT PRIMARY KEY AUTO_INCREMENT,
               NAME VARCHAR(20) NOT NULL UNIQUE
               );
               """;
        try {
              DBconnection.statement.execute(createTable);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void createCustomerTable(){
        String createCustomer = """
                CREATE TABLE IF NOT EXISTS CUSTOMER(
                ID INT PRIMARY KEY AUTO_INCREMENT,
                NAME VARCHAR(50) NOT NULL UNIQUE,
                RENTED_CAR_ID INT,
                CONSTRAINT fk_car FOREIGN KEY (RENTED_CAR_ID)
                REFERENCES CAR(ID)
                ON DELETE SET NULL
                ON UPDATE CASCADE
                );
                """;
        try {
            DBconnection.statement.execute(createCustomer);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static void insertCompanyQuery(String name){
        String insert = String.format("""
                INSERT INTO COMPANY (NAME)
                VALUES('%s');
                """, name);
        try {
            DBconnection.statement.execute(insert);
            System.out.println("The company was created!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public static void createCarTable(){
       /* String dropCar = """
                DROP TABLE IF EXISTS CAR;
                """;

        */
        String insertCar = """
                CREATE TABLE IF NOT EXISTS CAR(
                ID INT PRIMARY KEY AUTO_INCREMENT,
                NAME VARCHAR(50) NOT NULL UNIQUE,
                COMPANY_ID INT NOT NULL,
                CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID)
                REFERENCES COMPANY(ID));
                """;
        try {
            DBconnection.statement.execute(insertCar);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static void insertCar(String nameCar , int id){
        String query = String.format("""
                INSERT INTO CAR(NAME, COMPANY_ID) VALUES ('%s',%d)
                """, nameCar, id);
        try{
            DBconnection.statement.execute(query);
            System.out.println("The car was added!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public  static  void insertCustomer(String name){
        String query = String.format("""
                INSERT INTO CUSTOMER(NAME)
                VALUES('%s');
                """, name);
        try {
            DBconnection.statement.execute(query);
            System.out.println("The customer was added!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static  int findIdCarForCompany(String nameCompany){
        int id = CompanyDAO.getIdFromList(nameCompany);
        int idSearch = 0;
        String query = String.format("""
                SELECT ID FROM CAR
                WHERE COMPANY_ID = %d
                 ;
                """,id);
        try {
            PreparedStatement statement = DBconnection.connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            idSearch =  set.getInt(1);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return  idSearch;
    }
    public static boolean findIfCustomerRentedCar(String customer){
        String query = String.format("""
                SELECT RENTED_CAR_ID
                FROM CUSTOMER
                WHERE NAME = '%s';
                """,customer);
        boolean isTrue = false;
        try {
            PreparedStatement statement = DBconnection.connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (true){
                assert resultSet != null;
                if (!resultSet.next()) break;
                if(resultSet.getInt(1) > 0){
                    isTrue = true;
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return isTrue;
    }
    public static String findCarNameWithID(int id){
        String nameSearch = "";
        String req = String.format("""
                 SELECT NAME FROM CAR WHERE ID = %d
                 """, id);
        try{
            PreparedStatement statement = DBconnection.connection.prepareStatement(req);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                nameSearch = set.getString(1);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return nameSearch;
    }
    public static void rentedCar(String customer, int idCar ,String nameCompany){
        String query = String.format("""
                UPDATE CUSTOMER
                SET RENTED_CAR_ID = %d
                WHERE NAME = '%s';
         
                """,idCar, customer);
        String nameOfCar = findCarNameWithID(idCar);
        System.out.println(nameOfCar);
        try {
            DBconnection.statement.execute(query);
            rentedCarCache.put(nameOfCar, nameCompany);
            System.out.printf("You rented '%s'\n", nameOfCar);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static void returnCar(String name){
        String  findRentedId = String.format("""
                SELECT RENTED_CAR_ID 
                FROM CUSTOMER
                WHERE NAME = '%s';
                """, name);
        String query = String.format("""
                UPDATE CUSTOMER
                SET RENTED_CAR_ID = NULL
                WHERE NAME = '%s';
                """, name);

        try {
            PreparedStatement statement = DBconnection.connection.prepareStatement(findRentedId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() && resultSet.getInt(1) > 0){
                System.out.println(resultSet.getInt(1));
                DBconnection.statement.execute(query);
                System.out.println("You've returned a rented car!");
            }
            else {
                System.out.println("You didn't rent a car!");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static List<Car> findAllCarOfCompany(int idOfCompany){
        List<Car> cars = new ArrayList<>();
        String query = String.format("""
               SELECT ID,NAME,COMPANY_ID FROM CAR
               WHERE COMPANY_ID = %d
                     AND ID NOT IN(
                     SELECT RENTED_CAR_ID
                     FROM CUSTOMER
                     WHERE RENTED_CAR_ID IS NOT NULL);
                """, idOfCompany);
        try{
            PreparedStatement statement = DBconnection.connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                cars.add(new Car(resultSet.getInt(1), resultSet.getString(2)));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
            return cars;
    }
    public static void myRentedCarReq(){

    }
    public static int finRentedIdOfCustomer(String nameOfCustomer){
        String query = String.format("""
               SELECT RENTED_CAR_ID FROM CUSTOMER WHERE NAME = '%s';
                """, nameOfCustomer);
        int idSearch = 0;
        try {
            PreparedStatement statement = DBconnection.connection.prepareStatement(query);
            ResultSet resSet = statement.executeQuery();
            while (true){
                assert resSet != null;
                if (!resSet.next()) break;
                idSearch = resSet.getInt(1);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return  idSearch;
    }
    public static String findCompanyNameWithCarName(String carName){
        String query = String.format("""
               SELECT COMPANY_ID FROM CAR WHERE NAME = '%s';
                """, carName);
        int idSearch = 0;
        String nameSearch = "";
        try {
            PreparedStatement statement = DBconnection.connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            assert resultSet != null;
            while (resultSet.next()) {
                idSearch = resultSet.getInt(1);
            }
            if(idSearch > 0){
                String findCompany = String.format("""
                SELECT NAME FROM COMPANY WHERE ID = '%d';
                """,idSearch);
                PreparedStatement statement1 = DBconnection.connection.prepareStatement(findCompany);
                ResultSet resultSet1 = statement1.executeQuery();
                while (resultSet1.next()){
                    nameSearch = resultSet1.getString(1);
                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return  nameSearch;
    }

   /** public static List<String> getCarNotRented(){
        List<String> carsNotRented = new ArrayList<>();
        String req = String.format("""
                SELECT * FROM CAR 
                WHERE 
                """);
    }
    **/


}
