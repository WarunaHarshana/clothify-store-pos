package com.clothify.repository.custom.impl;

import com.clothify.db.DbConnection;
import com.clothify.model.Product;
import com.clothify.repository.custom.ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public List<Product> getAll() throws SQLException {
        String sql = "SELECT * FROM products WHERE is_active = TRUE ORDER BY product_id DESC";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {

                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Product> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM products WHERE is_active = TRUE AND (product_code LIKE ? OR name LIKE ?) ORDER BY product_id DESC";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {

                String text = "%" + (keyword == null ? "" : keyword.trim()) + "%";
                pstm.setString(1, text);
                pstm.setString(2, text);

                try (ResultSet rs = pstm.executeQuery()) {
                    List<Product> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(mapRow(rs));
                    }
                    return list;
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean save(Product product) throws SQLException {
        String sql = "INSERT INTO products (product_code, name, unit_price, quantity, is_active) VALUES (?,?,?,?,TRUE)";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, product.getProductCode());
                pstm.setString(2, product.getName());
                pstm.setDouble(3, product.getUnitPrice());
                pstm.setInt(4, product.getQuantity());
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String sql = "UPDATE products SET name=?, unit_price=?, quantity=? WHERE product_id=?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, product.getName());
                pstm.setDouble(2, product.getUnitPrice());
                pstm.setInt(3, product.getQuantity());
                pstm.setInt(4, product.getProductId());
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean delete(int productId) throws SQLException {
        String sql = "UPDATE products SET is_active=FALSE WHERE product_id=?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, productId);
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean updateQuantity(int productId, int newQuantity) throws SQLException {
        String sql = "UPDATE products SET quantity=? WHERE product_id=?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, newQuantity);
                pstm.setInt(2, productId);
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public String generateNextCode() throws SQLException {
        String sql = "SELECT product_code FROM products ORDER BY product_id DESC LIMIT 1";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {

                if (rs.next()) {
                    String lastCode = rs.getString(1);
                    int num = Integer.parseInt(lastCode.substring(1));
                    return String.format("P%03d", num + 1);
                }
                return "P001";
            }
        } catch (Exception e) {
            return "P001";
        }
    }

    @Override
    public int getProductCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE is_active = TRUE";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getLowStockCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE is_active = TRUE AND quantity < 10";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Product findById(int productId) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ? AND is_active = TRUE";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, productId);
                try (ResultSet rs = pstm.executeQuery()) {
                    return rs.next() ? mapRow(rs) : null;
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("product_id"),
                rs.getString("product_code"),
                rs.getString("name"),
                rs.getDouble("unit_price"),
                rs.getInt("quantity"),
                rs.getBoolean("is_active")
        );
    }
}
