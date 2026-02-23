package repository.custom;

import model.User;

import java.sql.SQLException;

public interface UserRepository {
    User findByUsername(String username) throws SQLException;
}
