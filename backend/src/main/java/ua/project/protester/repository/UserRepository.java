package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.project.protester.constants.SqlTemplates;
import ua.project.protester.model.User;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<User> findUserByEmail(String userEmail) {
        try {
            User user = jdbcTemplate.queryForObject(SqlTemplates.FIND_USER_BY_EMAIL,
                    new String[]{userEmail},
                    (rs, rowNum) -> new User(
                            rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getBoolean(5),
                            rs.getString(6)));
            return Optional.ofNullable(user);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<String> findUserEmailByTokenValue(String tokenValue) {
        try {
            String userEmail = jdbcTemplate.queryForObject(SqlTemplates.FIND_USER_EMAIL_BY_TOKEN_VALUE,
                    new String[]{tokenValue},
                    String.class);
            return Optional.ofNullable(userEmail);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void updatePassword(User user, String newUserPassword) {
        jdbcTemplate.update(SqlTemplates.UPDATE_USER_PASSWORD, newUserPassword, user.getId());
    }
}
