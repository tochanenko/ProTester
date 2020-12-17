package ua.project.protester.repository.result;

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
import ua.project.protester.model.RunResult;
import ua.project.protester.utils.PropertyExtractor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:queries/run-result.properties")
@Slf4j
public class RunResultRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;

    public RunResult saveUserRunResult(RunResult userRunResult) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("saving  run result with user {}", userRunResult.getUserId());

        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveRunResult"),
                new MapSqlParameterSource()
                        .addValue("user_id", userRunResult.getUserId()),
                keyHolder,
                new String[]{"id"});
        Integer id = (Integer) keyHolder.getKey();
        userRunResult.setId(id.longValue());

        saveTestCaseResults(userRunResult.getTestCaseResult(), userRunResult.getId());
        return userRunResult;
    }

    private void saveTestCaseResults(List<Integer> results, Long runId) {
         results.forEach(result -> saveTestCaseResult(result, runId));
    }

    private void saveTestCaseResult(Integer result, Long runId) {
        log.info("saving  test case result of run result with run id {}", runId);

        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveRunResultParams"),
                new MapSqlParameterSource()
                        .addValue("id", runId)
                        .addValue("test_case_result_id", result));
    }

    public Optional<RunResult> findRunResultById(Long id) {
        try {
            RunResult runResult = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findRunResultById"),
                    new MapSqlParameterSource().addValue("id", id),
                    (rs, rowNum) -> new RunResult(
                            rs.getLong("id"),
                            rs.getLong("user_id"))
            );
            if (runResult == null) {
                return Optional.empty();
            }

            runResult.setTestCaseResult(findTestCaseResultsByRunResultId(runResult.getId()));
            return Optional.of(runResult);
        } catch (DataAccessException e) {
            log.warn("environment with id {} was`nt found", id);
            return Optional.empty();
        }
    }

    private List<Integer> findTestCaseResultsByRunResultId(Long id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", id);

        return namedParameterJdbcTemplate.queryForList(PropertyExtractor.extract(env, "findTestCaseResults"),
                namedParams, Integer.class);
    }

    public List<RunResult> findAll() {
        try {
            List<RunResult> results = namedParameterJdbcTemplate.query(
                    PropertyExtractor.extract(env, "findAll"),
                    (rs, rowNum) -> new RunResult(
                            rs.getLong("id"),
                            rs.getLong("user_id"))
            );
            if (results.size() == 0) {
                return Collections.emptyList();
            }

            results.forEach(runResult -> runResult.setTestCaseResult(findTestCaseResultsByRunResultId(runResult.getId())));
            return results;
        } catch (EmptyResultDataAccessException e) {
            log.warn("result were`nt found");
            return Collections.emptyList();
        }
    }
}
