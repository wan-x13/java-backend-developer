package carsharing;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CarDAO implements CarModel{
    static List<Car> cars;
    CompanyDAO company = new CompanyDAO();
    public CarDAO(){
        cars = new ArrayList<>();
    }
    @Override
    public List<Car> getAllCar() {
        try{
            PreparedStatement ps =DBconnection.connection.prepareStatement("SELECT * FROM COMPANY ORDER BY ID");
            ResultSet res = ps.executeQuery();
            while (res.next()){
                cars.add(new Car(res.getInt(1), res.getString(2)));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return cars;
    }

    @Override
    public void addCar(String name , String companyName) {
        int id = CompanyDAO.getIdFromList(companyName);
        try {
          if(id != 0){
              Query.insertCar(name, id);
          }

      }catch (Exception e){
          System.out.println(e.getMessage());
      }
    }

    public static int getIdFromList( String carName){
        int id = 0;
        for(Car car : cars){
            if(car.getName().equals(carName)){
                id = car.getId();
                break;
            }
        }
        return  id;
    }
}
