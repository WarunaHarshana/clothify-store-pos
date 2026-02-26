package com.clothify.service;

import com.clothify.model.User;

import java.sql.SQLException;

import java.util.List;

public interface UserService {
    User authenticate(String username, String password) throws SQLException;

    boolean addUser(User user) throws SQLException;

    boolean updateUser(User user) throws SQLException;

    boolean deleteUser(int userId) throws SQLException;

    List<User> getAllUsers() throws SQLException;
}
