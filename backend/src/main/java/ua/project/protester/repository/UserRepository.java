package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.constants.SqlTemplates;
import ua.project.protester.model.User;
import ua.project.protester.utils.UserRowMapper;

import java.util.*;

@Repository
@PropertySource("classpath:queries/user.properties")
@RequiredArgsConstructor
public class UserRepository implements CrudRepository<User> {

    private final UserRowMapper rowMapper;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment environment;

        @Override
    public int save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource namedParams = new MapSqlParameterSource();

        namedParams.addValue("role_id", entity.getRole().getId());
        namedParams.addValue("user_username", entity.getUsername());
        namedParams.addValue("user_email", entity.getEmail());
        namedParams.addValue("user_password", entity.getPassword());
        namedParams.addValue("user_active", entity.isActive());
        namedParams.addValue("user_first_name", entity.getFirstName());
        namedParams.addValue("user_last_name", entity.getLastName());

        int update = namedJdbcTemplate.update(environment.getProperty("saveUser"), namedParams, keyHolder, new String[]{"user_id"});

        Integer id = (Integer) (keyHolder.getKeys().get("user_id"));
        entity.setId(id.longValue());

        return update;
    }

    @Override
    public Optional<User> findById(Long id) {

        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", id);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(environment.getProperty("findUserById"), namedParams, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return namedJdbcTemplate.query(environment.getProperty("findAllUsers"), rowMapper);
    }

    @Override
    public void update(User entity) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("role_id", entity.getRole().getId());
        namedParams.addValue("user_username", entity.getUsername());
        namedParams.addValue("user_email", entity.getEmail());
        namedParams.addValue("user_password", entity.getPassword());
        namedParams.addValue("user_active", entity.isActive());
        namedParams.addValue("user_first_name", entity.getFirstName());
        namedParams.addValue("user_last_name", entity.getLastName());
        namedParams.addValue("user_id", entity.getId());

        namedJdbcTemplate.update(environment.getProperty("updateUser"), namedParams);
    }

    @Override
    public void delete(User entity) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", entity.getId());
        namedJdbcTemplate.update(environment.getProperty("deleteUser"), namedParams);
    }

    public void deactivate(Long id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", id);
        namedJdbcTemplate.update(environment.getProperty("deactivateUser"), namedParams);

    }


    public List<User> findUsersByRoleId(Long roleId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("role_id", roleId);
            return namedJdbcTemplate.query(environment.getProperty("findUsersByRoleId"), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<User> findUsersByRoleName(String roleName) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("role_name", roleName);
            return namedJdbcTemplate.query(environment.getProperty("findUsersByRoleName"), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Optional<User> findUserByEmail(String email) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_email", email);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(environment.getProperty("findUserByEmail"), namedParams, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findUserByUsername(String username) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_username", username);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(environment.getProperty("findUserByUsername"), namedParams, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<User> findUserByRolename(String roleName) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("role_name", roleName);
            return namedJdbcTemplate.query(environment.getProperty("findUsersByRoleName"), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<User> findUsersByName(String name) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_first_name", name);
            return namedJdbcTemplate.query(environment.getProperty("findUsersByName"), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<User> findUsersBySurname(String surname) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_last_name", surname);
            return namedJdbcTemplate.query(environment.getProperty("findUsersBySurname"), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<User> findAllPagination(int limit, int offset) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("limit", limit);
            namedParams.put("offset", offset);
            return namedJdbcTemplate.query(environment.getProperty("findAllUsersPagination"), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }


    public Optional<String> findUserEmailByTokenValue(String tokenValue) {
        try {
            String userEmail = namedJdbcTemplate.queryForObject(
                    SqlTemplates.FIND_USER_EMAIL_BY_TOKEN_VALUE,
                    new MapSqlParameterSource().addValue("value", tokenValue),
                    String.class);
            return Optional.ofNullable(userEmail);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void updatePassword(User user, String newUserPassword) {
        namedJdbcTemplate.update(SqlTemplates.UPDATE_USER_PASSWORD,
                new MapSqlParameterSource()
                        .addValue("password", newUserPassword)
                        .addValue("id", user.getId()));
    }
}
