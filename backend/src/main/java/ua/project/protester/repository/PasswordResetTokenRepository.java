package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.PasswordResetToken;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Repository
@PropertySource("classpath:queries/password-reset-token.properties")
@RequiredArgsConstructor
public class PasswordResetTokenRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment environment;

    private final Logger logger = LoggerFactory.getLogger(PasswordResetTokenRepository.class);

    private static final String PROPERTY_NOT_FOUND_TEMPLATE = "Could not find property '%s' in queries/password-reset-token.properties";

    public void save(PasswordResetToken token) {
        String queryPropertyName = "saveToken";
        try {
            namedParameterJdbcTemplate.update(
                    Objects.requireNonNull(environment.getProperty(queryPropertyName)),
                    new BeanPropertySqlParameterSource(token));
        } catch (NullPointerException e) {
            logger.warn(String.format(
                    PROPERTY_NOT_FOUND_TEMPLATE,
                    queryPropertyName));
        }
    }

    public Optional<Date> findExpiryDateByValue(String tokenValue) {
        String queryPropertyName = "findTokenExpiryDateByTokenValue";
        try {
            Date expiryDate = namedParameterJdbcTemplate.queryForObject(
                    Objects.requireNonNull(environment.getProperty("findTokenExpiryDateByTokenValue")),
                    new MapSqlParameterSource().addValue("value", tokenValue),
                    Date.class);
            return Optional.ofNullable(expiryDate);
        } catch (DataAccessException e) {
            return Optional.empty();
        } catch (NullPointerException e) {
            logger.warn(String.format(
                    PROPERTY_NOT_FOUND_TEMPLATE,
                    queryPropertyName));
            return Optional.empty();
        }
    }
}
