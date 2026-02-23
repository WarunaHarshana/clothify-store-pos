package service.custom;

import model.User;

import java.sql.SQLException;

public interface UserService {
    User authenticate(String username, String password) throws SQLException;
}
