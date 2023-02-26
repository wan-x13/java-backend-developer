package carsharing;

public class Car {

    private int id ;
    private String name ;
    public  Car(int id ,String name){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
