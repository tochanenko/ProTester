package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.PropertyExtractor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:queries/environment.properties")
public class EnvironmentRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;

    public ua.project.protester.model.Environment saveEnvironment(ua.project.protester.model.Environment environment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("saving {} environment with description {}", environment.getName(), environment.getDescription());

        String saveEnvironment = "INSERT INTO environment(name, description, password, username, url)" +
                "VALUES (:name, :description, :password, :username, :url)";
        namedParameterJdbcTemplate.update(
                //PropertyExtractor.extract(env, "saveEnvironment"),
                saveEnvironment,
                new MapSqlParameterSource()
                        .addValue("name", environment.getName())
                        .addValue("description", environment.getDescription())
                        .addValue("password", environment.getPassword())
                        .addValue("username", environment.getUsername())
                        .addValue("url", environment.getUrl()),
                keyHolder,
                new String[]{"id"});
        Integer id = (Integer) keyHolder.getKey();
        environment.setId(id.longValue());
        return environment;
    }


    public ua.project.protester.model.Environment updateEnvironment(ua.project.protester.model.Environment environment) {

        String updateEnvironment = "UPDATE environment" +
                "  SET name = :name,description = :description,password = :password," +
                "username = :username, url = :url WHERE id = :id";
        namedParameterJdbcTemplate.update(
               // PropertyExtractor.extract(env, "updateEnvironment"),
                updateEnvironment,
                new MapSqlParameterSource()
                        .addValue("id", environment.getId())
                        .addValue("name", environment.getName())
                        .addValue("description", environment.getDescription())
                        .addValue("password", environment.getPassword())
                        .addValue("username", environment.getUsername())
                        .addValue("url", environment.getUrl()));
        log.info("updating {} environment with description {} and id {}", environment.getName(), environment.getDescription(), environment.getId());

        return environment;
    }

    public void deleteEnvironmentById(Long id) {
        String delete = "DELETE FROM environment WHERE id = :id";
        namedParameterJdbcTemplate.update(
                //PropertyExtractor.extract(env, "deleteEnvironment"),
                delete,
                new MapSqlParameterSource()
                        .addValue("id", id));
    }

    public Optional<ua.project.protester.model.Environment> findEnvironmentById(Long id) {
        try {
            String findById = " SELECT e.id, e.name, e.description, e.password, e.username, e.url " +
                    "FROM environment e WHERE id = :id";
            ua.project.protester.model.Environment environment = namedParameterJdbcTemplate.queryForObject(
                    //PropertyExtractor.extract(env, "findEnvironmentById"),
                    findById,
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

            String findAll = "SELECT * FROM ENVIRONMENT";
            List<ua.project.protester.model.Environment> environment = namedParameterJdbcTemplate.query(
                   // PropertyExtractor.extract(env, "findAll"),
                    findAll,
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

    public List<ua.project.protester.model.Environment> findAll(Pagination pagination) {
        System.out.println("IN REPO  " + pagination.getSearchField());
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("pageSize", pagination.getPageSize());
        namedParams.addValue("offset", pagination.getOffset());
        namedParams.addValue("environmentName", pagination.getSearchField() + "%");

        try {
            List<ua.project.protester.model.Environment> environmentList = namedParameterJdbcTemplate.query(
                    PropertyExtractor.extract(env, "findAllPaginated"),
                    namedParams,
                    (rs, rowNum) -> new ua.project.protester.model.Environment(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("url"))
            );
            if (environmentList.size() == 0) {
                return Collections.emptyList();
            }
            return environmentList;
        } catch (EmptyResultDataAccessException e) {
            log.warn("environments were`nt found");
            return Collections.emptyList();
        }
    }

    public Long count(Pagination pagination) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("environmentName", pagination.getSearchField() + "%");

        return namedParameterJdbcTemplate.queryForObject(PropertyExtractor.extract(env, "countEnvironment"),
                namedParams, Long.class);
    }

}
