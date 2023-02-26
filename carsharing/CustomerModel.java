package carsharing;

import java.util.List;

public interface CustomerModel {
   List<Customer> getAllCustomer();
   void addCustomer(String name);
   void rentCarCustomer(String name , String nameOfCompany, int id);
   void returnCarCustomer(String name);

}
