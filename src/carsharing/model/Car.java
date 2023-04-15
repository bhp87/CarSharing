package carsharing.model;

public class Car {
    int id;
    String name;
    int company_id;


    public Car(int id, String name, int company_id) {
        this.id = id;
        this.name = name;
        this.company_id = company_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {

        return ". " + name;
    }
}
