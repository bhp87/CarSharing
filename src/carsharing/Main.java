package carsharing;

import carsharing.config.Db;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;
import carsharing.service.CarDaoImpl;
import carsharing.service.CompanyDaoImpl;
import carsharing.service.CustomerDaoImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
    private static Customer customer = new Customer();
    static Scanner scanner;
    static CompanyDaoImpl companyDao;
    static CarDaoImpl carDao;
    static CustomerDaoImpl customerDao;

    public static void main(String[] args) throws SQLException {
        Db database = new Db(args);
        scanner = new Scanner(System.in);
        companyDao = new CompanyDaoImpl(database.getConnection());
        carDao = new CarDaoImpl(database.getConnection());
        customerDao = new CustomerDaoImpl(database.getConnection());

        while (true) {
            System.out.println("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");
            int userChoice = Integer.parseInt(scanner.nextLine());
            switch (userChoice) {
                case 0 -> {
                    scanner.close();
                    database.exit();
                    return;
                }
                case 1 -> managerMenu(scanner, companyDao, carDao);
                case 2 -> customerMenu(scanner, customerDao, companyDao, carDao);
                case 3 -> createCustomer(scanner, customerDao);
            }

        }

    }

    private static void managerMenu(Scanner scanner, CompanyDaoImpl companyDao, CarDaoImpl carDao) throws SQLException {
        while (true) {
            System.out.println("1. Company list\n2. Create a company\n0. Back");
            int managerChoice = Integer.parseInt(scanner.nextLine());

            switch (managerChoice) {
                case 0 -> {
                    return;
                }
                case 1 -> displayCompanyList(companyDao, carDao, scanner);
                case 2 -> createCompany(companyDao, scanner);
                default -> System.out.println("Invalid choice, try again!");
            }
        }
    }

    private static void customerMenu(Scanner scanner, CustomerDaoImpl customerDao, CompanyDaoImpl companyDao, CarDaoImpl carDao) throws SQLException {
        List<Customer> customers = customerDao.getCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            return;
        }
        System.out.println("Customer list:");
        customers.forEach(System.out::println);
        System.out.println("0. Back");
        int customerChoice = Integer.parseInt(scanner.nextLine());
        if (customerChoice == 0) {
            return;
        }
        customer = customers.get(customerChoice - 1);
        displayCustomerMenu(scanner, companyDao, carDao, customerDao);
    }

    private static void displayCustomerMenu(Scanner scanner, CompanyDaoImpl companyDao, CarDaoImpl carDao, CustomerDaoImpl customerDao) throws SQLException {
        while (true) {
            System.out.println("1. Rent a car\n2. Return a rented car\n3. My rented car\n0. Back");
            int customerMenuChoice = Integer.parseInt(scanner.nextLine());
            switch (customerMenuChoice) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    if (customer.getRentedCarId() != 0) {
                        System.out.println("You've already rented a car!");
                        continue;
                    }
                    displayCompanyList(companyDao, carDao, scanner, customerDao);
                }
                case 2 -> returnRentedCar(customerDao);
                case 3 -> showRentedCar(carDao);
            }
        }


    }


    private static void showRentedCar(CarDaoImpl carDao) throws SQLException {
        if (customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
            return;
        }
        System.out.println("Your rented car:");
        Car car = carDao.getCarById(customer.getRentedCarId());
        System.out.println(car);
        System.out.println("Company:");

        System.out.println(companyDao.getCompanyById(car.getCompany_id()));

    }

    private static void returnRentedCar(CustomerDaoImpl customerDao) throws SQLException {
        if (customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
            return;
        }
        customerDao.returnRentedCar(customer);
        customer.setRentedCarId(0);
        System.out.println("You've returned a rented car!");

    }


    private static void displayCompanyList(CompanyDaoImpl companyDao, CarDaoImpl carDao, Scanner scanner, CustomerDaoImpl customerDao) throws SQLException {
        List<Company> companies = companyDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        System.out.println("Choose the company:");
        companies.forEach(System.out::println);
        System.out.println("0. Back");

        int companyChoice = Integer.parseInt(scanner.nextLine());
        if (companyChoice != 0 && null != customer) {
            displayCarMenuForCustomer(companies.get(companyChoice - 1), carDao, scanner, customerDao);
        }
    }

    private static void displayCompanyList(CompanyDaoImpl companyDao, CarDaoImpl carDao, Scanner scanner) throws SQLException {
        List<Company> companies = companyDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        System.out.println("Choose the company:");
        companies.forEach(System.out::println);
        System.out.println("0. Back");

        int companyChoice = Integer.parseInt(scanner.nextLine());
        if (companyChoice != 0) {
            displayCarMenuForManager(companies.get(companyChoice - 1), carDao, scanner);
        }

    }

    private static void displayCarMenuForCustomer(Company company, CarDaoImpl carDao, Scanner scanner, CustomerDaoImpl customerDao) throws SQLException {
        List<Car> cars = carDao.getAllCars(company.getId());
        while (true) {
            System.out.println("Choose a car:");
            displayAvailableCarList(company, carDao);
            int carChoice = Integer.parseInt(scanner.nextLine());
            if (carChoice != 0) {

                customerDao.rentACar(cars.get(carChoice - 1));
                customer.setRentedCarId(cars.get(carChoice - 1).getId());

                System.out.println("You rented " + "'" + cars.get(carChoice - 1).getName() + "'");
                return;
            }

        }
    }

    private static void displayCarMenuForManager(Company company, CarDaoImpl carDao, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println(company.getName() + " company");
            System.out.println("1. Car list\n2. Create a car\n0. Back");
            int carChoice = Integer.parseInt(scanner.nextLine());

            switch (carChoice) {
                case 0 -> {
                    return;
                }
                case 1 -> displayCarList(company, carDao);
                case 2 -> createCar(company, carDao, scanner);
                default -> System.out.println("Invalid choice, try again!");
            }
        }
    }

    private static void displayAvailableCarList(Company company, CarDaoImpl carDao) throws SQLException {
        List<Car> cars = carDao.getAvailableCars(company.getId());
        System.out.println(cars);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            return;
        }
        IntStream.rangeClosed(1, cars.size())
                .mapToObj(i -> i + ". " + cars.get(i - 1).getName())
                .forEach(System.out::println);
    }

    private static void displayCarList(Company company, CarDaoImpl carDao) throws SQLException {
        List<Car> cars = carDao.getAllCars(company.getId());
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            return;
        }

        IntStream.rangeClosed(1, cars.size())
                .mapToObj(i -> i + ". " + cars.get(i - 1).getName())
                .forEach(System.out::println);
    }

    private static void createCompany(CompanyDaoImpl companyDao, Scanner scanner) throws SQLException {
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        companyDao.createCompany(companyName);
        System.out.println("The company was created!");
    }

    private static void createCar(Company company, CarDaoImpl carDao, Scanner scanner) throws SQLException {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        carDao.createCar(carName, company.getId());
        System.out.println("The car was added!");
    }

    private static void createCustomer(Scanner scanner, CustomerDaoImpl customerDao) throws SQLException {
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        customerDao.createCustomer(customerName);
        System.out.println("The customer was added!");
    }
}