package carsharing.service;

import carsharing.model.Car;
import carsharing.model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface ICustomer {
    void createCustomer(String customerName) throws SQLException;

    List<Customer> getCustomers() throws SQLException;

    void rentACar(Car car) throws SQLException;

    void returnRentedCar(Customer customer) throws SQLException;
}
