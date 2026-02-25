package com.clothify.repository.custom;

import com.clothify.model.User;

import java.sql.SQLException;

import java.util.List;

public interface UserRepository {
    User findByUsername(String username) throws SQLException;

    boolean save(User user) throws SQLException;

    boolean update(User user) throws SQLException;

    boolean delete(int userId) throws SQLException;

    List<User> findAll() throws SQLException;
}
