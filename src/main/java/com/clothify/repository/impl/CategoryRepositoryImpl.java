package com.clothify.repository.impl;

import com.clothify.db.DbConnection;
import com.clothify.model.Category;
import com.clothify.repository.CategoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    @Override
    public List<Category> getAll() throws SQLException {
        String sql = "SELECT * FROM categories WHERE is_active = TRUE ORDER BY category_name";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {
                List<Category> list = new ArrayList<>();
                while (rs.next()) list.add(mapRow(rs));
                return list;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Category> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM categories WHERE is_active = TRUE AND (category_name LIKE ? OR description LIKE ?) ORDER BY category_name";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                String text = "%" + (keyword == null ? "" : keyword.trim()) + "%";
                pstm.setString(1, text);
                pstm.setString(2, text);
                try (ResultSet rs = pstm.executeQuery()) {
                    List<Category> list = new ArrayList<>();
                    while (rs.next()) list.add(mapRow(rs));
                    return list;
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean save(Category category) throws SQLException {
        String sql = "INSERT INTO categories (category_name, description, is_active) VALUES (?,?,TRUE)";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, category.getCategoryName());
                pstm.setString(2, category.getDescription());
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean update(Category category) throws SQLException {
        String sql = "UPDATE categories SET category_name=?, description=? WHERE category_id=?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, category.getCategoryName());
                pstm.setString(2, category.getDescription());
                pstm.setInt(3, category.getCategoryId());
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean delete(int categoryId) throws SQLException {
        String sql = "UPDATE categories SET is_active = FALSE WHERE category_id=?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, categoryId);
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("category_id"),
                rs.getString("category_name"),
                rs.getString("description"),
                rs.getBoolean("is_active")
        );
    }
}
