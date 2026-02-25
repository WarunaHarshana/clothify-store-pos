package com.clothify.repository.custom.impl;

import com.clothify.db.DbConnection;
import com.clothify.model.Supplier;
import com.clothify.repository.custom.SupplierRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepositoryImpl implements SupplierRepository {

    @Override
    public List<Supplier> getAll() throws SQLException {
        String sql = "SELECT * FROM suppliers WHERE is_active = TRUE ORDER BY supplier_id DESC";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {
                List<Supplier> list = new ArrayList<>();
                while (rs.next()) list.add(mapRow(rs));
                return list;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Supplier> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM suppliers WHERE is_active = TRUE AND (name LIKE ? OR contact_person LIKE ? OR phone LIKE ?) ORDER BY supplier_id DESC";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                String text = "%" + (keyword == null ? "" : keyword.trim()) + "%";
                pstm.setString(1, text);
                pstm.setString(2, text);
                pstm.setString(3, text);
                try (ResultSet rs = pstm.executeQuery()) {
                    List<Supplier> list = new ArrayList<>();
                    while (rs.next()) list.add(mapRow(rs));
                    return list;
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean save(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO suppliers (name, contact_person, phone, email, address) VALUES (?,?,?,?,?)";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setString(1, supplier.getName());
                pstm.setString(2, supplier.getContactPerson());
                pstm.setString(3, supplier.getPhone());
                pstm.setString(4, supplier.getEmail());
                pstm.setString(5, supplier.getAddress());
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean update(Supplier supplier) throws SQLException {
        String sql = "UPDATE suppliers SET name=?, contact_person=?, phone=?, email=?, address=? WHERE supplier_id=?";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setString(1, supplier.getName());
                pstm.setString(2, supplier.getContactPerson());
                pstm.setString(3, supplier.getPhone());
                pstm.setString(4, supplier.getEmail());
                pstm.setString(5, supplier.getAddress());
                pstm.setInt(6, supplier.getSupplierId());
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean delete(int supplierId) throws SQLException {
        String sql = "UPDATE suppliers SET is_active=FALSE WHERE supplier_id=?";
        try {
            Connection conn = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setInt(1, supplierId);
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private Supplier mapRow(ResultSet rs) throws SQLException {
        return new Supplier(
                rs.getInt("supplier_id"),
                rs.getString("name"),
                rs.getString("contact_person"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getBoolean("is_active")
        );
    }
}

