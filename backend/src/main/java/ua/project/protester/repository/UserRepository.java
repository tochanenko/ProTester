package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.User;
import ua.project.protester.utils.UserRowMapper;

import java.util.*;

@Repository
@PropertySource("classpath:queries/user.properties")
@RequiredArgsConstructor
@Slf4j
public class UserRepository implements CrudRepository<User> {

    private final UserRowMapper rowMapper;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment environment;

    private static final String PROPERTY_NOT_FOUND_TEMPLATE = "Could not find property '%s' in queries/user.properties";

    @Override
    public int save(User entity) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();

        namedParams.addValue("role_id", entity.getRole().getId());
        namedParams.addValue("user_username", entity.getUsername());
        namedParams.addValue("user_email", entity.getEmail());
        namedParams.addValue("user_password", entity.getPassword());
        namedParams.addValue("user_active", entity.isActive());
        namedParams.addValue("user_first_name", entity.getFirstName());
        namedParams.addValue("user_last_name", entity.getLastName());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        log.info("saving user {}", entity);
        int update = namedJdbcTemplate.update(Objects.requireNonNull(environment.getProperty("saveUser")), namedParams, keyHolder, new String[]{"user_id"});

        Integer id = (Integer) (keyHolder.getKeys().get("user_id"));
        entity.setId(id.longValue());

        return update;
    }

    @Override
    public Optional<User> findById(Long id) {

        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", id);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(Objects.requireNonNull(environment.getProperty("findUserById")), namedParams, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            log.warn("user with id {} was`nt found", id);
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return namedJdbcTemplate.query(Objects.requireNonNull(environment.getProperty("findAllUsers")), rowMapper);
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

        log.info("updating user {}", entity);
        namedJdbcTemplate.update(Objects.requireNonNull(environment.getProperty("updateUser")), namedParams);
    }

    @Override
    public void delete(User entity) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", entity.getId());
        namedJdbcTemplate.update(Objects.requireNonNull(environment.getProperty("deleteUser")), namedParams);
    }

    public void deactivate(Long id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", id);
        namedJdbcTemplate.update(Objects.requireNonNull(environment.getProperty("deactivateUser")), namedParams);
    }

    public void activate(Long id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", id);
        namedJdbcTemplate.update(Objects.requireNonNull(environment.getProperty("activateUser")), namedParams);
    }


    public List<User> findUsersByRoleId(Long roleId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("role_id", roleId);
            return namedJdbcTemplate.query(Objects.requireNonNull(environment.getProperty("findUsersByRoleId")), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.warn("user with role id {} was`nt found", roleId);
            return null;
        }
    }

    public Optional<User> findUserByEmail(String email) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_email", email);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(Objects.requireNonNull(environment.getProperty("findUserByEmail")), namedParams, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            log.warn("user with email {} was`nt found", email);
            return Optional.empty();
        }
    }

    public Optional<User> findUserByUsername(String username) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_username", username);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(Objects.requireNonNull(environment.getProperty("findUserByUsername")), namedParams, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            log.warn("user with username {} was`nt found", username);
            return Optional.empty();
        }
    }

    public List<User> findUserByRolename(String roleName) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("role_name", roleName);
            return namedJdbcTemplate.query(Objects.requireNonNull(environment.getProperty("findUsersByRoleName")), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.warn("user with rolename {} was`nt found", roleName);
            return new ArrayList<>();
        }
    }

    public List<User> findUsersByName(String name) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_first_name", name);
            return namedJdbcTemplate.query(Objects.requireNonNull(environment.getProperty("findUsersByName")), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.warn("user with name {} was`nt found", name);
            return new ArrayList<>();
        }
    }

    public List<User> findUsersBySurname(String surname) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_last_name", surname);
            return namedJdbcTemplate.query(Objects.requireNonNull(environment.getProperty("findUsersBySurname")), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.warn("user with surname {} was`nt found", surname);
            return new ArrayList<>();
        }
    }

    public List<User> findAllPagination(int limit, int offset) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("limit", limit);
            namedParams.put("offset", offset);
            return namedJdbcTemplate.query(Objects.requireNonNull(environment.getProperty("findAllUsersPagination")), namedParams, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }


    public Optional<String> findUserEmailByTokenValue(String tokenValue) {
        String queryPropertyName = "findUserEmailByResetPasswordTokenValue";
        try {
            String userEmail = namedJdbcTemplate.queryForObject(
                    Objects.requireNonNull(environment.getProperty("findUserEmailByResetPasswordTokenValue")),
                    new MapSqlParameterSource().addValue("value", tokenValue),
                    String.class);
            return Optional.ofNullable(userEmail);
        } catch (DataAccessException e) {
            return Optional.empty();
        } catch (NullPointerException e) {
            log.warn(String.format(
                    PROPERTY_NOT_FOUND_TEMPLATE,
                    queryPropertyName));
            return Optional.empty();
        }
    }

    public void updatePassword(User user, String newUserPassword) {
        String queryPropertyName = "updateUserPassword";
        try {
            namedJdbcTemplate.update(
                    Objects.requireNonNull(environment.getProperty("updateUserPassword")),
                    new MapSqlParameterSource()
                            .addValue("password", newUserPassword)
                            .addValue("id", user.getId()));
        } catch (NullPointerException e) {
            log.warn(String.format(
                    PROPERTY_NOT_FOUND_TEMPLATE,
                    queryPropertyName));
        }
    }
}
