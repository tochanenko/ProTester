package ua.project.protester.utils;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.project.protester.model.Role;
import ua.project.protester.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("user_id"));
        user.setName(resultSet.getString("user_name"));
        user.setPassword(resultSet.getString("user_password"));
        user.setEmail(resultSet.getString("user_email"));
        user.setActive(resultSet.getBoolean("user_active"));
        user.setRole(new Role(resultSet.getLong("role_id")));
        user.setFullName(resultSet.getString("user_full_name"));
        return user;
    }
}
