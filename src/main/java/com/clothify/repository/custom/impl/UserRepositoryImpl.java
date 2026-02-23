package com.clothify.repository.custom.impl;

import com.clothify.db.DbConnection;
import com.clothify.model.User;
import com.clothify.repository.custom.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND is_active = TRUE";
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, username);
            ResultSet rs = pstm.executeQuery();
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
            return null;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }
}

