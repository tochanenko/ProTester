package ua.project.protester.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import ua.project.protester.model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet resultSet, int i) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("role_id"));
        role.setName(resultSet.getString("role_name"));
        return role;
    }
}
