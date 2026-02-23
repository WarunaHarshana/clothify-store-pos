package com.clothify.service.custom;

import com.clothify.model.User;

import java.sql.SQLException;

public interface UserService {
    User authenticate(String username, String password) throws SQLException;
}

