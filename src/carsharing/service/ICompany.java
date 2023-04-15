package carsharing.service;

import carsharing.model.Company;

import java.sql.SQLException;
import java.util.List;

public interface ICompany {
    List<Company> getAllCompanies() throws SQLException;

    Company getCompanyById(int id) throws SQLException;

    void createCompany(String companyName) throws SQLException;
}
