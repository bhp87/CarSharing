package carsharing.service;

import carsharing.model.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements ICompany {
    Connection connection;

    public CompanyDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public List<Company> getAllCompanies() throws SQLException {

        PreparedStatement listCompaniesStatement = this.connection.prepareStatement("SELECT * FROM COMPANY ORDER BY ID");
        ResultSet resultSet = listCompaniesStatement.executeQuery();


        List<Company> companies = new ArrayList<>();
        while (resultSet.next()) {
            Company company = new Company(resultSet.getInt("ID"), resultSet.getString("NAME"));
            companies.add(company);
        }
        resultSet.close();
        listCompaniesStatement.close();
        return companies;
    }

    @Override
    public Company getCompanyById(int id) throws SQLException {
        PreparedStatement companyStatement = this.connection.prepareStatement("SELECT * FROM COMPANY WHERE ID = ?");
        companyStatement.setInt(1, id);
        ResultSet resultSet = companyStatement.executeQuery();
        Company company = null;
        if (resultSet.next()) {
            company = new Company(resultSet.getInt("ID"), resultSet.getString("NAME"));
        }
        resultSet.close();
        companyStatement.close();
        return company;
    }

    @Override
    public void createCompany(String companyName) throws SQLException {
        PreparedStatement createCompanyStatement = this.connection.prepareStatement("INSERT INTO COMPANY (NAME) VALUES (?)");
        createCompanyStatement.setString(1, companyName);
        createCompanyStatement.executeUpdate();
        // Close the statement
        createCompanyStatement.close();
    }
}