package carsharing;

public class Company {

    private int id ;
    private String name ;
    public Company(int id , String name){
        this.name = name;
        this.id = id;

    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
