package com.clothify.repository.impl;

import com.clothify.db.DbConnection;
import com.clothify.model.Employee;
import com.clothify.repository.EmployeeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    @Override
    public List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM employees WHERE is_active = TRUE ORDER BY employee_id DESC";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {
                List<Employee> list = new ArrayList<>();
                while (rs.next()) list.add(mapRow(rs));
                return list;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Employee> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM employees WHERE is_active = TRUE AND (full_name LIKE ? OR phone LIKE ? OR email LIKE ?) ORDER BY employee_id DESC";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                String text = "%" + (keyword == null ? "" : keyword.trim()) + "%";
                pstm.setString(1, text);
                pstm.setString(2, text);
                pstm.setString(3, text);
                try (ResultSet rs = pstm.executeQuery()) {
                    List<Employee> list = new ArrayList<>();
                    while (rs.next()) list.add(mapRow(rs));
                    return list;
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean save(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (full_name, phone, email, role, hire_date) VALUES (?,?,?,?,?)";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setString(1, employee.getFullName());
                pstm.setString(2, employee.getPhone());
                pstm.setString(3, employee.getEmail());
                pstm.setString(4, employee.getRole());
                pstm.setString(5, employee.getHireDate());
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET full_name=?, phone=?, email=?, role=?, hire_date=? WHERE employee_id=?";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setString(1, employee.getFullName());
                pstm.setString(2, employee.getPhone());
                pstm.setString(3, employee.getEmail());
                pstm.setString(4, employee.getRole());
                pstm.setString(5, employee.getHireDate());
                pstm.setInt(6, employee.getEmployeeId());
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean delete(int employeeId) throws SQLException {
        String sql = "UPDATE employees SET is_active=FALSE WHERE employee_id=?";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setInt(1, employeeId);
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("employee_id"),
                rs.getString("full_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("role"),
                rs.getString("hire_date"),
                rs.getBoolean("is_active")
        );
    }
}

