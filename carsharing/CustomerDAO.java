package carsharing;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements CustomerModel{
   static List<Customer> customers;
   public CustomerDAO(){
       customers = new ArrayList<>();
   }
    @Override
    public List<Customer> getAllCustomer() {
        try{
            PreparedStatement ps =DBconnection.connection.prepareStatement("SELECT * FROM CUSTOMER ORDER BY ID");
            ResultSet res = ps.executeQuery();
            while (res.next()){
                customers.add(new Customer(res.getInt(1), res.getString(2)));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return customers;
    }
    @Override
    public void addCustomer(String nameCustomer) {
                Query.insertCustomer(nameCustomer);
    }
    @Override
    public void rentCarCustomer(String name ,String nameOfCompany , int idCar) {
       Query.rentedCar(name , idCar , nameOfCompany);
    }
    @Override
    public void returnCarCustomer(String name) {
       Query.returnCar(name);
    }
    public void myRentedCar(){

    }


}
