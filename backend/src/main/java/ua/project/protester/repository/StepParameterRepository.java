package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.project.protester.utils.PropertyExtractor;

import java.util.HashMap;
import java.util.Map;

@PropertySource("classpath:queries/step-parameter.properties")
@Repository
@RequiredArgsConstructor
@Slf4j
public class StepParameterRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;

    public void saveAll(Integer id, Map<String, String> parameters) {
        parameters.forEach((key, value) -> save(id, key, value));
    }

    public void save(Integer id, Map.Entry<String, String> entry) {
        save(id, entry.getKey(), entry.getValue());
    }

    public void save(Integer id, String key, String value) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveStepParameter"),
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("key", key)
                        .addValue("value", value));
    }

    public Map<String, String> findAllDataSetParamsId(int id) {
        Map<String, String> parameters = new HashMap<>();
        namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findAllByStepId"),
                new MapSqlParameterSource().addValue("id", id),
                (rs, rowNum) -> parameters.put(
                        rs.getString("key"),
                        rs.getString("value")));
        return parameters;
    }
}
