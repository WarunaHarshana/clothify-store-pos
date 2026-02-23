package com.clothify.repository.custom;

import com.clothify.model.User;

import java.sql.SQLException;

public interface UserRepository {
    User findByUsername(String username) throws SQLException;
}

