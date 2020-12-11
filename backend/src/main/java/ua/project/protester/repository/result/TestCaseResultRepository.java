package ua.project.protester.repository.result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.executable.result.ActionResult;
import ua.project.protester.model.executable.result.ResultStatus;
import ua.project.protester.model.executable.result.TestCaseResult;
import ua.project.protester.repository.StatusRepository;
import ua.project.protester.utils.PropertyExtractor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@PropertySource("classpath:queries/test-case-result.properties")
@Repository
@RequiredArgsConstructor
@Slf4j
public class TestCaseResultRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final ActionResultRepository actionResultRepository;
    private final StatusRepository statusRepository;

    public TestCaseResult saveTestCaseResult(TestCaseResult result) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveTestCaseResult"),
                new BeanPropertySqlParameterSource(result),
                keyHolder,
                new String[]{"test_case_result_id"});
        result.setId((Integer) keyHolder.getKey());
        result.setStatus(statusRepository.getLabelById(result.getStatusId()));
        return result;
    }

    public void updateTestCaseResultStatus(Integer id, ResultStatus status) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateTestCaseResultStatus"),
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("statusId", statusRepository.getIdByLabel(status)));
    }

    public void updateTestCaseResultEndDate(Integer id, OffsetDateTime endDate) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateTestCaseResultEndDate"),
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("endDate", endDate));
    }

    public Optional<TestCaseResult> findTestCaseResultById(Integer id) {
        TestCaseResult result;
        try {
            result = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findTestCaseResultById"),
                    new MapSqlParameterSource().addValue("id", id),
                    new BeanPropertyRowMapper<>(TestCaseResult.class));
        } catch (DataAccessException e) {
            return Optional.empty();
        }

        if (result == null) {
            return Optional.empty();
        }

        result.setStatus(statusRepository.getLabelById(result.getStatusId()));
        List<ActionResult> innerResults = actionResultRepository.findActionResultsByTestCaseResultId(id);
        result.setInnerResults(innerResults);
        return Optional.of(result);
    }
}
