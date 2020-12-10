package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.executable.result.ResultStatus;
import ua.project.protester.utils.PropertyExtractor;

@PropertySource("classpath:queries/status.properties")
@Repository
@RequiredArgsConstructor
@Slf4j
public class StatusRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;

    public Integer getPassedId() {
        return getIdByLabel(ResultStatus.PASSED);
    }

    public Integer getFailedId() {
        return getIdByLabel(ResultStatus.FAILED);
    }

    private Integer getIdByLabel(ResultStatus label) {
        return namedParameterJdbcTemplate.queryForObject(
                PropertyExtractor.extract(env, "getId"),
                new MapSqlParameterSource()
                        .addValue("label", label),
                Integer.class);
    }
}
