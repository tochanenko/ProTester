package ua.project.protester.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class AbstractRepository<T> implements CrudRepository<T> {

    private   NamedParameterJdbcTemplate namedJdbcTemplate;
    private   Environment environment;
    private   RowMapper<T> rowMapper;
    private   String[] template;

    public AbstractRepository() {
    }

    @Autowired
    public AbstractRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment environment, RowMapper<T> rowMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.environment = environment;
        this.rowMapper = rowMapper;
    }

    @Override
    public int save(T entity) {
        return 0;
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public void update(T entity) {

    }

    @Override
    public void delete(T entity) {

    }

    @Override
    public Optional<T> findById(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", id);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(environment.getProperty(template[0]), namedParams, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
