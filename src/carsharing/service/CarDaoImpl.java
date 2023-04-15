package carsharing.service;

import carsharing.model.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements ICar {
    Connection connection;

    public CarDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Car> getAllCars(int companyId) throws SQLException {
        //
        PreparedStatement listCarsStatement = this.connection.prepareStatement("SELECT * FROM CAR WHERE COMPANY_ID = ?");
        listCarsStatement.setInt(1, companyId);
        ResultSet resultSet = listCarsStatement.executeQuery();


        List<Car> cars = new ArrayList<>();
        while (resultSet.next()) {
            Car car = new Car(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("COMPANY_ID"));
            cars.add(car);
        }
        resultSet.close();
        listCarsStatement.close();
        return cars;
    }


    @Override
    public void createCar(String carName, int companyId) throws SQLException {
        PreparedStatement createCarStatement = this.connection.prepareStatement("INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)");
        createCarStatement.setString(1, carName);
        createCarStatement.setInt(2, companyId);
        createCarStatement.executeUpdate();
        // Close the statement
        createCarStatement.close();
    }

    @Override
    public Car getCarById(Integer rentedCarId) throws SQLException {
        PreparedStatement carStatement = this.connection.prepareStatement("SELECT * FROM CAR WHERE ID = ?");
        carStatement.setInt(1, rentedCarId.intValue());
        ResultSet resultSet = carStatement.executeQuery();
        Car car = null;
        if (resultSet.next()) {
            car = new Car(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("COMPANY_ID"));
        }
        resultSet.close();
        carStatement.close();
        return car;
    }

    @Override
    public List<Car> getAvailableCars(int id) throws SQLException {
        PreparedStatement availableCarsStatement = this.connection.prepareStatement(
                "  SELECT *" +
                        "                    FROM CAR LEFT JOIN CUSTOMER \n" +
                        "                    ON car.id = CUSTOMER.RENTED_CAR_ID \n" +
                        "                    WHERE CUSTOMER.NAME IS NULL;"
        );
        ResultSet resultSet = availableCarsStatement.executeQuery();

        List<Car> cars = new ArrayList<>();
        while (resultSet.next()) {
            Car car = new Car(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getInt("COMPANY_ID"));
            cars.add(car);
        }
        resultSet.close();
        availableCarsStatement.close();
        return cars;
    }


}
