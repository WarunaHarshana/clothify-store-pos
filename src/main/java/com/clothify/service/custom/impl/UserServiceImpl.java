package com.clothify.service.custom.impl;

import com.clothify.model.User;
import com.clothify.repository.custom.UserRepository;
import com.clothify.repository.custom.impl.UserRepositoryImpl;
import com.clothify.service.custom.UserService;
import com.clothify.util.PasswordUtil;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authenticate(String username, String password) throws SQLException {
        User user = userRepository.findByUsername(username);
        if (user == null) return null;
        if (!user.isActive()) return null;
        return PasswordUtil.verifyPassword(password, user.getPasswordHash()) ? user : null;
    }
}
