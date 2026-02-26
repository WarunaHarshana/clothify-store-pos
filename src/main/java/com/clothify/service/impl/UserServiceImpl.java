package com.clothify.service.impl;

import com.clothify.model.User;
import com.clothify.repository.UserRepository;
import com.clothify.repository.impl.UserRepositoryImpl;
import com.clothify.service.UserService;
import com.clothify.util.PasswordUtil;

import java.sql.SQLException;

import java.util.List;

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
        if (user == null)
            return null;
        if (!user.isActive())
            return null;
        return PasswordUtil.verifyPassword(password, user.getPasswordHash()) ? user : null;
    }

    @Override
    public boolean addUser(User user) throws SQLException {
        user.setPasswordHash(PasswordUtil.hashPassword(user.getPasswordHash())); // password_hash field initially holds
                                                                                 // plain text from UI
        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        if (user.getPasswordHash() != null && !user.getPasswordHash().trim().isEmpty()) {
            user.setPasswordHash(PasswordUtil.hashPassword(user.getPasswordHash()));
        } else {
            // Retain old password hash if no new password was provided
            List<User> all = userRepository.findAll();
            for (User u : all) {
                if (u.getUserId() == user.getUserId()) {
                    user.setPasswordHash(u.getPasswordHash());
                    break;
                }
            }
        }
        return userRepository.update(user);
    }

    @Override
    public boolean deleteUser(int userId) throws SQLException {
        return userRepository.delete(userId);
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        return userRepository.findAll();
    }
}
