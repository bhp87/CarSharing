package carsharing.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Db {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/";

    private String databaseFileName;
    private Connection connection;
    private Statement statement;

    public Db(String[] args) {
        this.databaseFileName = "carsharing"; // default database file name if -databaseFileName argument is not given
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-databaseFileName")) {
                    this.databaseFileName = args[i + 1];
                }
            }
        }
    }

    public Connection getConnection() {
        if (null == this.connection) {
            try {
                //Register JDBC driver
                Class.forName(JDBC_DRIVER);

                //Open a connection
                connection = DriverManager.getConnection(DB_URL + this.databaseFileName);
                statement = connection.createStatement();
                //   Drop the COMPANY and CAR tables if they exist after rerun of the program
                statement.execute("DROP TABLE IF EXISTS CAR");
                statement.execute("DROP TABLE IF EXISTS COMPANY");
                statement.execute("DROP TABLE IF EXISTS CUSTOMER");

                // Create the COMPANY table
                statement.execute("CREATE TABLE COMPANY (ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(255) NOT NULL UNIQUE)");

                // Create the CAR table
                statement.execute("CREATE TABLE CAR" +
                        " (ID INT PRIMARY KEY AUTO_INCREMENT," +
                        " NAME VARCHAR(255) NOT NULL UNIQUE," +
                        " COMPANY_ID INT NOT NULL," +
                        " FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))");
                //Create the Costumer table

                statement.execute("CREATE TABLE CUSTOMER (\n" +
                        "    ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                        "    NAME VARCHAR(255) UNIQUE NOT NULL,\n" +
                        "    RENTED_CAR_ID INT,\n" +
                        "    FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)\n" +
                        ")");
                //enable auto commit
                connection.setAutoCommit(true);
            } catch (
                    SQLException se) {
                //Handle errors for JDBC
                System.out.println("lala");
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            }
        }
        return this.connection;
    }

    public void exit() {
        try {
            // Close the statement
            if (this.statement != null) {
                this.statement.close();
            }

            // Close the connection
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing statement or connection: " + e.getMessage());
        }
    }

}