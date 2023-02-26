package carsharing;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Menu {
    Scanner scanner = new Scanner(System.in);
    CompanyModel companyDA0 = new CompanyDAO();
    CustomerModel customerDAO = new CustomerDAO();
    CarModel carDAO = new CarDAO();
    public  void initLogManager(){
        DBconnection.createConnection();
        Query.createCarTable();
        Query.createTableCompany();
        Query.createCustomerTable();

        int input ;
        while (true){
            System.out.println("""
                1. Log in as a manager
                2. Log in as a customer
                3. Create a customer
                0. Exit
                """);
            input = scanner.nextInt();
            if(input == 1){
                LogManager();
            }
            if(input == 2){
                getCustomers();
            }
            if(input == 3){
                createCustomer();
            }
            if(input == 0){
                try {
                    Optional<Statement> statement = Optional.ofNullable(DBconnection.statement);
                    if(statement.isPresent()){
                        DBconnection.closeConnection();
                    }
                }catch (SQLException | NullPointerException e){
                    System.out.println(e.getMessage());
                }
                break;
            }

        }
    }

    private void LogManager(){

        int inputLog ;
        do{
            System.out.println("""
                1. Company list
                2. Create a company
                0. Back
                """);
            inputLog = scanner.nextInt();
            switch (inputLog){
                case 1 -> {
                    getAllCompany(companyDA0);

                }
                case 2 ->createCompany();
            }

        }while (inputLog != 0);

    }
    private void getAllCompany(CompanyModel companyDA0){
        if(companyDA0.complanyList().isEmpty()){
            System.out.println("The company list is empty!");
        }else{
            System.out.println("Choose the company:");
            int input;
            List<Company> tempList = companyDA0.complanyList();
            tempList.stream().map(item ->item.getId()+". "+item.getName())
                    .forEach(System.out::println);
               System.out.println("0. Back");
               input = scanner.nextInt();
               if(input >= 1){
                   String name = CompanyDAO.findCompanyNameWithID(input);
                   companyProfil(name, input);
               }
               if (input == 0){
                   LogManager();
               }
        }
    }
    public  void companyProfil(String nameOfCompany, int id){
        int idInput = 0;
        do {
            System.out.printf("""
                    '%s' company
                    1. Car list
                    2. Create a car
                    0. Back
                    """, nameOfCompany);
                idInput = scanner.nextInt();
                System.out.println(idInput);

            switch (idInput){
                case 1->{
                    System.out.println("Car list:");
                   var list =  Query.findAllCarOfCompany(id);
                   if(list.isEmpty()){
                       System.out.println("The car list is empty!");
                   }
                   else {
                       int i = 1;
                       for(Car car : list){
                           System.out.println(i+". "+car.getName());
                           i += 1;
                       }
                   }
                }
                case 2-> {
                    System.out.println("Enter the car name:");
                    String nameOfCar = (scanner.next()+scanner.nextLine());
                    carDAO.addCar(nameOfCar , nameOfCompany);
                }
            }
            System.out.println();
        }while (idInput != 0);
    }
    private void createCompany(){
        System.out.println("Enter the company name:");
        String name;
        boolean isTrue = true;
        while (isTrue){
            name = scanner.nextLine();
            if(!name.isBlank()){
                companyDA0.addCompany(name);
                isTrue = false;
            }
        }
    }
    public void getCustomers(){
        List<Customer> customers = customerDAO.getAllCustomer();
        int input;
        String nameSearch;
        if(customers.isEmpty()){
            System.out.println("The customer list is empty!");
        }
        else {
            System.out.println("The customer list:");
            customers.stream()
                    .map(item -> item.getId()+". "+ item.getName())
                    .forEach(System.out::println);
            System.out.println("0. Back");
            input = scanner.nextInt();
            var optionalCustomer = customers.stream()
                    .filter(item-> item.getId() == input)
                    .findAny();
            if(optionalCustomer.isPresent()){
                 nameSearch = optionalCustomer.get().getName();
                 rentMenu(nameSearch);
            }
        }
    }
    public void createCustomer(){
        System.out.println("Enter the customer name:");
        String nameOfCustomer = (scanner.next()+scanner.nextLine());
        customerDAO.addCustomer(nameOfCustomer);
    }
    public  void rentMenu(String nameOfCustomer){
        int input;
        do {
            System.out.println("""
                1. Rent a car
                2. Return a rented car
                3. My rented car
                0. Back
                """);
            input = scanner.nextInt();
            if(Query.findIfCustomerRentedCar(nameOfCustomer) && input == 1){
                System.out.println("You've already rented a car!");
            }
            else if(input == 1){
                rentCar(nameOfCustomer);
            } else if (input == 2) {
                returnCar(nameOfCustomer);
            }else if(input == 3){
                int id = Query.finRentedIdOfCustomer(nameOfCustomer);
                if(id != 0){
                    String nameOfcar = Query.findCarNameWithID(id);
                    myRentedCar(nameOfcar);
                }else{
                    System.out.println("You didn't rent a car!");
                }

            }
        }while (input != 0);

    }
    public void rentCar(String nameOfCustomer){
        System.out.println("Choose a company:");
        companyDA0.complanyList().stream()
                        .map(it->it.getId()+". "+it.getName())
                .forEach(System.out::println);
        System.out.println("0. Back");
        int input = scanner.nextInt();
        if(input == 0){
            rentMenu(nameOfCustomer);
        }
        if(input >= 1){
            getCarsOfCompanyUtilsForRent(input, nameOfCustomer);
        }


    }
    public void getCarsOfCompanyUtilsForRent(int idCompany, String nameOfCustomer){
        var list = Query.findAllCarOfCompany(idCompany);
        int input;
        if(list.isEmpty()){
            System.out.println("No available cars in the 'Company name'");
        }else{
                System.out.println("Choose a car:");
                list.stream().map(it->it.getId()+". "+it.getName())
                        .forEach(System.out::println);
                System.out.println("0. Back");
                input = scanner.nextInt();
                if(input == 0){
                    rentMenu(nameOfCustomer);
                }
                else {
                    String nameOfCompany = CompanyDAO.findCompanyNameWithID(idCompany);
                    customerDAO.rentCarCustomer(nameOfCustomer,nameOfCompany , input);
                }
            }


    }
    public void returnCar(String name){
        customerDAO.returnCarCustomer(name);
    }
    public void myRentedCar(String nameOfCar){
        var map = Query.rentedCarCache;
        var nameOfCompany = Query.findCompanyNameWithCarName(nameOfCar);
            System.out.printf("""
                    Your rented car:
                    %s
                    Company:
                    %s
                    """, nameOfCar , nameOfCompany);
    }
}
