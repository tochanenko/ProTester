package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
@PropertySource("classpath:queries/action-parameter.properties")
@RequiredArgsConstructor
public class ActionParameterRepository {

    private static final String PROPERTY_NOT_FOUND_TEMPLATE = "Could not find property '%s' in queries/action-parameter.properties";
    private final Logger logger = LoggerFactory.getLogger(ActionParameterRepository.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;

    public void saveParameters(Integer actionId, Map<String, String> params) {
        params.forEach((key, value) -> saveParameter(actionId, key, value));
    }

    public Map<String, String> findAllParametersByActionId(Integer actionId) {
        String propertyName = "findActionParametersByActionId";
        try {
            Map<String, String> parameters = new HashMap<>();
            namedParameterJdbcTemplate.query(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new MapSqlParameterSource()
                            .addValue("actionId", actionId), rs -> {
                        parameters.put(rs.getString(1), rs.getString(2));
                    });
            return parameters;
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
            return Collections.emptyMap();
        } catch (DataAccessException e) {
            logger.warn(e.toString());
            return Collections.emptyMap();
        }
    }

    private void saveParameter(int actionId, String key, String value) {
        String propertyName = "saveActionParameter";
        try {
            namedParameterJdbcTemplate.update(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new MapSqlParameterSource()
                            .addValue("actionId", actionId)
                            .addValue("key", key)
                            .addValue("value", value));
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
        }
    }

    public void deleteActionFromActionParameter(Integer actionId) {
        String propertyName = "deleteActionParametersByActionId";
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("actionId", actionId);
        namedParameterJdbcTemplate.update(Objects.requireNonNull(env.getProperty(propertyName)), namedParams);
    }
}
