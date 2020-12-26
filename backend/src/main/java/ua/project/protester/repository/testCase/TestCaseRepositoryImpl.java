package ua.project.protester.repository.testCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.exception.TestCaseCreateException;
import ua.project.protester.model.TestCase;
import ua.project.protester.repository.DataSetRepository;
import ua.project.protester.response.LightTestCaseResponse;
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.PropertyExtractor;
import ua.project.protester.utils.testcase.TestCaseRowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:queries/test-case.properties")
public class TestCaseRepositoryImpl implements TestCaseRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    private final TestCaseRowMapper testCaseRowMapper;
    private final DataSetRepository dataSetRepository;

    @Override
    public TestCase create(TestCase testCase) throws TestCaseCreateException {
        log.info("IN TestCaseRepositoryImpl create - testCase: {}", testCase);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveTestCase"),
                new MapSqlParameterSource()
                        .addValue("name", testCase.getName())
                        .addValue("project_id", testCase.getProjectId())
                        .addValue("description", testCase.getDescription())
                        .addValue("author_id", testCase.getAuthorId())
                        .addValue("scenario_id", testCase.getScenarioId())
                        .addValue("data_set_id", testCase.getDataSetId()),
                keyHolder);

        Integer id = (Integer) (Optional.ofNullable(keyHolder.getKeys())
                .orElseThrow(TestCaseCreateException::new)
                .get("test_case_id"));

        testCase.setId(id.longValue());

        log.info("IN TestCaseRepositoryImpl - saved testCase {}", testCase);
        return testCase;
    }

    @Override
    public TestCase update(TestCase testCase) {
        log.info("IN TestCaseRepositoryImpl update - testCase: {}", testCase);

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateTestCase"),
                new MapSqlParameterSource()
                        .addValue("test_case_id", testCase.getId())
                        .addValue("name", testCase.getName())
                        .addValue("description", testCase.getDescription())
                        .addValue("scenario_id", testCase.getScenarioId())
                        .addValue("data_set_id", testCase.getDataSetId()));

        log.info("updating testCase {}", testCase.getName());
        return testCase;
    }

    @Override
    public void delete(Long id) {
        log.info("IN TestCaseRepositoryImpl delete id={}", id);

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "deleteTestCase"),
                new MapSqlParameterSource().addValue("test_case_id", id));

        log.info("testCase id={} was deleted", id);
    }

    @Override
    public Optional<TestCase> findById(Long id) {
        log.info("IN TestCaseRepositoryImpl findById id={}", id);

        try {

            return Optional.ofNullable(namedJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findTestCaseById"),
                    new MapSqlParameterSource()
                            .addValue("test_case_id", id),
                    testCaseRowMapper));

        } catch (EmptyResultDataAccessException e) {
            log.warn("testCase with id {} was not found", id);
            return Optional.empty();
        }
    }

    @Override
    public List<TestCase> findAllProjectTestCases(Pagination pagination, Long projectId) {
        log.info("IN TestCaseRepositoryImpl findAllProjectTestCases pagination={}, projectId={}",
                pagination, projectId);

        return namedJdbcTemplate.query(
                PropertyExtractor.extract(env, "findAllByProject"),
                new MapSqlParameterSource()
                        .addValue("pageSize", pagination.getPageSize())
                        .addValue("offset", pagination.getOffset())
                        .addValue("filterName", pagination.getSearchField() + "%")
                        .addValue("project_id", projectId),
                testCaseRowMapper);
    }

    @Override
    public Optional<TestCase> findProjectTestCase(Long projectId, Long testCaseId) {
        log.info("IN TestCaseRepositoryImpl findAllProjectTestCases  projectId={} testCaseId={}", projectId, testCaseId);

        try {
            return Optional.ofNullable(
                    namedJdbcTemplate.queryForObject(
                            PropertyExtractor.extract(env, "findTestCaseByProjectIdAndTestCaseId"),
                            new MapSqlParameterSource()
                                    .addValue("project_id", projectId)
                                    .addValue("test_case_id", testCaseId),
                            testCaseRowMapper));

        } catch (EmptyResultDataAccessException e) {
            log.warn("test cases were`nt found");
            return Optional.empty();
        }
    }

    @Override
    public Long getCountTestCase(Pagination pagination, Long projectId) {
        log.info("IN TestCaseRepositoryImpl getCountTestCase pagination={}, projectId={}",
                pagination, projectId);

        return namedJdbcTemplate.queryForObject(
                PropertyExtractor.extract(env, "getCountTestCase"),
                new MapSqlParameterSource()
                        .addValue("filterName", pagination.getSearchField() + "%")
                        .addValue("project_id", projectId),
                Long.class);
    }

    @Override
    public List<LightTestCaseResponse> findTestCasesByTestScenarioId(int id) {
        try {
            return namedJdbcTemplate.query(
                    PropertyExtractor.extract(env, "findTestCasesByTestScenarioId"),
                    new MapSqlParameterSource().addValue("id", id),
                    new BeanPropertyRowMapper<>(LightTestCaseResponse.class));
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

}
