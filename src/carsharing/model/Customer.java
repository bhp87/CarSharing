package carsharing.model;

public class Customer {
    int id;
    String name;
    Integer rentedCarId;

    public Customer(int id, String name, Integer rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public Customer() {

    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}
