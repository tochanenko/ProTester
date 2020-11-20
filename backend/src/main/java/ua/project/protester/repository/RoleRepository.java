package ua.project.protester.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.Role;
import ua.project.protester.utils.RoleRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@PropertySource("classpath:queries/role.properties")
@Repository
public class RoleRepository implements CrudRepository<Role> {

    private  NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment environment;
    private final RoleRowMapper roleRowMapper;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public RoleRepository(Environment environment, RoleRowMapper roleRowMapper) {
        this.environment = environment;
        this.roleRowMapper = roleRowMapper;
    }

    @Override
    public int save(Role entity) {
        return 0;
    }

    @Override
    public Optional<Role> findById(Long id) {

        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("role_id", id);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(environment.getProperty("findRoleById"), namedParams, roleRowMapper));
        } catch (EmptyResultDataAccessException e) {

            return Optional.empty();
        }
    }

    @Override
    public List<Role> findAll() {
        return namedJdbcTemplate.query(environment.getProperty("findAllRoles"), roleRowMapper);
    }

    @Override
    public void update(Role entity) {

    }

    @Override
    public void delete(Role entity) {

    }

    public Optional<Role> findRoleByName(String name) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("role_name", name);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(environment.getProperty("findRoleByName"), namedParams, roleRowMapper));
        } catch (EmptyResultDataAccessException e) {

            return Optional.empty();
        }
    }

}
