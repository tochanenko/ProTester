package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.utils.PropertyExtractor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EnvironmentRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;

    public ua.project.protester.model.Environment saveEnvironment(ua.project.protester.model.Environment environment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("saving {} environment with description {}", environment.getName(), environment.getDescription());

        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveEnvironment"),
                new MapSqlParameterSource()
                        .addValue("name", environment.getName())
                        .addValue("description", environment.getDescription())
                        .addValue("password", environment.getPassword())
                        .addValue("username", environment.getUsername())
                        .addValue("url", environment.getUrl()),
                keyHolder,
                new String[]{"data_set_id"});
        Integer id = (Integer) keyHolder.getKey();
        environment.setId(id.longValue());
        return environment;
    }

    public Optional<ua.project.protester.model.Environment> findEnvironmentById(Long id) {
        try {
            ua.project.protester.model.Environment environment = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findEnvironmentById"),
                    new MapSqlParameterSource().addValue("id", id),
                    (rs, rowNum) -> new ua.project.protester.model.Environment(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("url"))
            );
            if (environment == null) {
                return Optional.empty();
            }

            return Optional.of(environment);
        } catch (DataAccessException e) {
            log.warn("environment with id {} was`nt found", id);
            return Optional.empty();
        }
    }

    public List<ua.project.protester.model.Environment> findAll() {
        try {
            List<ua.project.protester.model.Environment> environment = namedParameterJdbcTemplate.query(
                    PropertyExtractor.extract(env, "findAll"),
                    (rs, rowNum) -> new ua.project.protester.model.Environment(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("url"))
            );
            if (environment.size() == 0) {
                return Collections.emptyList();
            }
            return environment;
        } catch (EmptyResultDataAccessException e) {
            log.warn("environments were`nt found");
            return Collections.emptyList();
        }
    }

}
