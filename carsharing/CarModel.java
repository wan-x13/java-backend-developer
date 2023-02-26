package carsharing;

import java.util.List;

public interface CarModel {
    List<Car> getAllCar();
    void addCar(String name , String companyName);
}
