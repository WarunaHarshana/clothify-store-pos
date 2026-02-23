package service.custom.impl;

import model.User;
import repository.custom.UserRepository;
import repository.custom.impl.UserRepositoryImpl;
import service.custom.UserService;
import util.PasswordUtil;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public User authenticate(String username, String password) throws SQLException {
        User user = userRepository.findByUsername(username);
        if (user == null) return null;
        if (!user.isActive()) return null;
        return PasswordUtil.verifyPassword(password, user.getPasswordHash()) ? user : null;
    }
}
