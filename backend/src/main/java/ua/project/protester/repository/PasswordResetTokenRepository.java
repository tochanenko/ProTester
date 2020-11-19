package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.project.protester.constants.SqlTemplates;
import ua.project.protester.model.PasswordResetToken;

import java.util.Date;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(PasswordResetToken token) {
        jdbcTemplate.update(SqlTemplates.SAVE_TOKEN,
                token.getUserId(),
                token.getValue(),
                token.getExpiryDate());
    }

    public Optional<Date> findExpiryDateByValue(String tokenValue) {
        try {
            Date expiryDate = jdbcTemplate.queryForObject(SqlTemplates.FIND_TOKEN_EXPIRY_DATE_BY_TOKEN_VALUE,
                    new String[]{tokenValue},
                    (rs, rowNum) -> rs.getDate(1));
            return Optional.ofNullable(expiryDate);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
