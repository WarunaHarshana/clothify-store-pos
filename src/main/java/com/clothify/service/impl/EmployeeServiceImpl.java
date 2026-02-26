package com.clothify.service.impl;

import com.clothify.model.Employee;
import com.clothify.repository.EmployeeRepository;
import com.clothify.repository.impl.EmployeeRepositoryImpl;
import com.clothify.service.EmployeeService;

import java.sql.SQLException;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    @Override
    public List<Employee> getAllEmployees() throws SQLException {
        return employeeRepository.getAll();
    }

    @Override
    public List<Employee> searchEmployees(String keyword) throws SQLException {
        return employeeRepository.search(keyword);
    }

    @Override
    public boolean addEmployee(Employee employee) throws SQLException {
        return employeeRepository.save(employee);
    }

    @Override
    public boolean updateEmployee(Employee employee) throws SQLException {
        return employeeRepository.update(employee);
    }

    @Override
    public boolean deleteEmployee(int employeeId) throws SQLException {
        return employeeRepository.delete(employeeId);
    }
}

