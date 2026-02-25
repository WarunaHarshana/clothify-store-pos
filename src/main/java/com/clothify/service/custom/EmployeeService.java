package com.clothify.service.custom;

import com.clothify.model.Employee;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees() throws SQLException;
    List<Employee> searchEmployees(String keyword) throws SQLException;
    boolean addEmployee(Employee employee) throws SQLException;
    boolean updateEmployee(Employee employee) throws SQLException;
    boolean deleteEmployee(int employeeId) throws SQLException;
}

