package com.clothify.repository.custom.impl;

import com.clothify.db.DbConnection;
import com.clothify.model.User;
import com.clothify.repository.custom.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND is_active = TRUE";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, username);
                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setPasswordHash(rs.getString("password_hash"));
                        user.setFullName(rs.getString("full_name"));
                        user.setRole(rs.getString("role"));
                        user.setActive(rs.getBoolean("is_active"));
                        return user;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean save(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, full_name, role, is_active) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, user.getUsername());
                pstm.setString(2, user.getPasswordHash());
                pstm.setString(3, user.getFullName());
                pstm.setString(4, user.getRole());
                pstm.setBoolean(5, user.isActive());
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean update(User user) throws SQLException {
        // We only update password_hash if it is not empty. If it's empty, we might not
        // want to change it.
        // Actually, for simplicity let's update all fields since User Management will
        // set them.
        String sql = "UPDATE users SET username=?, password_hash=?, full_name=?, role=?, is_active=? WHERE user_id=?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, user.getUsername());
                pstm.setString(2, user.getPasswordHash());
                pstm.setString(3, user.getFullName());
                pstm.setString(4, user.getRole());
                pstm.setBoolean(5, user.isActive());
                pstm.setInt(6, user.getUserId());
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean delete(int userId) throws SQLException {
        String sql = "UPDATE users SET is_active=FALSE WHERE user_id=?";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, userId);
                return pstm.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users WHERE is_active = TRUE";
        List<User> userList = new ArrayList<>();
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            try (PreparedStatement pstm = connection.prepareStatement(sql);
                    ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash")); // Note: returning hashes directly to UI
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    user.setActive(rs.getBoolean("is_active"));
                    userList.add(user);
                }
            }
            return userList;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }
}
