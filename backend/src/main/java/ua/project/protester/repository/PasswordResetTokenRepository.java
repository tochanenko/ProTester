package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.PasswordResetToken;
import ua.project.protester.utils.PropertyExtractor;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
@PropertySource("classpath:queries/password-reset-token.properties")
@RequiredArgsConstructor
public class PasswordResetTokenRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment environment;

    public void save(PasswordResetToken token) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(environment, "saveToken"),
                new BeanPropertySqlParameterSource(token));
    }

    public Optional<OffsetDateTime> findExpiryDateByValue(String tokenValue) {
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(environment, "findTokenExpiryDateByTokenValue"),
                    new MapSqlParameterSource().addValue("value", tokenValue),
                    OffsetDateTime.class));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
