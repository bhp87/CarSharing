package carsharing.service;

import carsharing.model.Car;
import carsharing.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements ICustomer {
    Connection connection;

    public CustomerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createCustomer(String customerName) throws SQLException {
        PreparedStatement createCarStatement = this.connection.prepareStatement("INSERT INTO CUSTOMER (NAME) VALUES (?)");
        createCarStatement.setString(1, customerName);
        createCarStatement.executeUpdate();
        // Close the statement
        createCarStatement.close();

    }

    @Override
    public List<Customer> getCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement listCustomersStatement = this.connection.prepareStatement("SELECT * FROM CUSTOMER ORDER BY ID");
             ResultSet resultSet = listCustomersStatement.executeQuery()) {
            while (resultSet.next()) {
                Customer customer = new Customer(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("RENTED_CAR_ID"));
                customers.add(customer);
            }
        }

        return customers;
    }

    @Override
    public void rentACar(Car car) throws SQLException {
        PreparedStatement createCustomerStatement = this.connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ?");
        createCustomerStatement.setInt(1, car.getId());
        createCustomerStatement.executeUpdate();
        // Close the statement
        createCustomerStatement.close();
    }

    @Override
    public void returnRentedCar(Customer customer) throws SQLException {
        PreparedStatement createCustomerStatement = this.connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?");
        createCustomerStatement.setNull(1, java.sql.Types.INTEGER);
        createCustomerStatement.setInt(2, customer.getId());
        createCustomerStatement.executeUpdate();
        createCustomerStatement.close();

    }

}
