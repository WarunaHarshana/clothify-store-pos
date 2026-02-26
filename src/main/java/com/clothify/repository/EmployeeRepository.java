package com.clothify.repository;

import com.clothify.model.Employee;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeRepository {
    List<Employee> getAll() throws SQLException;
    List<Employee> search(String keyword) throws SQLException;
    boolean save(Employee employee) throws SQLException;
    boolean update(Employee employee) throws SQLException;
    boolean delete(int employeeId) throws SQLException;
}

