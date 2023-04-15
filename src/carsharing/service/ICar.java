package carsharing.service;

import carsharing.model.Car;

import java.sql.SQLException;
import java.util.List;

public interface ICar {
    List<Car> getAllCars(int companyId) throws SQLException;


    void createCar(String carName, int companyMenuChoice) throws SQLException;

    Car getCarById(Integer rentedCarId) throws SQLException;

    List<Car> getAvailableCars(int id) throws SQLException;
}
