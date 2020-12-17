package ua.project.protester.repository.testCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.TestCase;
import ua.project.protester.repository.DataSetRepository;
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
    public TestCase create(TestCase testCase, List<Long> dataSet) {
        log.info("IN TestCaseRepositoryImpl create - testCase: {}, dataSet: {}", testCase, dataSet);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveTestCase"),
                new MapSqlParameterSource()
                        .addValue("name", testCase.getName())
                        .addValue("project_id", testCase.getProjectId())
                        .addValue("description", testCase.getDescription())
                        .addValue("author_id", testCase.getAuthorId())
                        .addValue("scenario_id", testCase.getScenarioId()),
                keyHolder,
                new String[]{"test_case_id"});

        log.info("saving testCase with name {}", testCase.getName());

        Integer id = (Integer) keyHolder.getKey();
        testCase.setId(id.longValue());

        saveDataSet(testCase.getId(), dataSet);
        return testCase;
    }

    @Override
    public TestCase update(TestCase testCase, List<Long> dataSet) {
        log.info("IN TestCaseRepositoryImpl update - testCase: {}, dataSet: {}", testCase, dataSet);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateTestCase"),
                new MapSqlParameterSource()
                        .addValue("test_case_id", testCase.getId())
                        .addValue("name", testCase.getName())
                        .addValue("description", testCase.getDescription())
                        .addValue("scenario_id", testCase.getScenarioId()),
                keyHolder,
                new String[]{"test_case_id"});

        log.info("updating testCase {}", testCase.getName());
        deleteDataSet(testCase.getId());
        saveDataSet(testCase.getId(), dataSet);
        return testCase;
    }

    @Override
    public void delete(Long id) {
        log.info("IN TestCaseRepositoryImpl delete id={}", id);
        deleteDataSet(id);

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "deleteTestCase"),
                new MapSqlParameterSource().addValue("test_case_id", id));

        log.info("testCase id={} was deleted", id);
    }

    @Override
    public Optional<TestCase> findById(Long id) {
        log.info("IN TestCaseRepositoryImpl findById id={}", id);

        try {
            Optional<TestCase> testCase = Optional.ofNullable(namedJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findTestCaseById"),
                    new MapSqlParameterSource()
                            .addValue("test_case_id", id),
                    testCaseRowMapper));

            testCase.ifPresent(aCase -> aCase.setDataSetList(dataSetRepository.findDataSetByTestCaseId(aCase.getId())));
            return testCase;
        } catch (EmptyResultDataAccessException e) {
            log.warn("testCase with id {} was not found", id);
            return Optional.empty();
        }
    }

    @Override
    public List<TestCase> findAllProjectTestCases(Pagination pagination, Long projectId) {
        log.info("IN TestCaseRepositoryImpl findAllProjectTestCases pagination={}, projectId={}",
                pagination, projectId);

        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("pageSize", pagination.getPageSize());
        namedParams.addValue("offset", pagination.getOffset());
        namedParams.addValue("filterName", pagination.getSearchField() + "%");
        namedParams.addValue("project_id", projectId);

        List<TestCase> testCaseList = namedJdbcTemplate.query(
                PropertyExtractor.extract(env, "findAllByProject"),
                namedParams,
                testCaseRowMapper);

        testCaseList.forEach(
                list -> list.setDataSetList(dataSetRepository.findDataSetByTestCaseId(list.getId()))
        );

        testCaseList.forEach(System.out::print);
        return testCaseList;
    }

    @Override
    public Optional<TestCase> findProjectTestCase(Long projectId, Long testCaseId) {
        log.info("IN TestCaseRepositoryImpl findAllProjectTestCases  projectId={} testCaseId={}", projectId, testCaseId);

        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("project_id", projectId);
        namedParams.addValue("test_case_id", testCaseId);

        try {
            TestCase testCase = namedJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findTestCaseByProjectIdAndTestCaseId"),
                    namedParams,
                    testCaseRowMapper);

            if (testCase != null) {
                testCase.setDataSetList(dataSetRepository.findDataSetByTestCaseId(testCaseId));
                return Optional.of(testCase);
            }

        } catch (EmptyResultDataAccessException e) {
            log.warn("test cases were`nt found");
            return Optional.empty();
        }

        return Optional.empty();
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

    private void saveDataSet(Long testCaseId, Long dataSetId) {
        log.info("IN TestCaseRepositoryImpl saveDataSet testCaseId={}, dataSetId={}", testCaseId, dataSetId);

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveTestCaseDataSet"),
                new MapSqlParameterSource()
                        .addValue("test_case_id", testCaseId)
                        .addValue("data_set_id", dataSetId));
    }

    public void saveDataSet(Long testCaseId, List<Long> dataSetId) {
        dataSetId.forEach(dataSet -> saveDataSet(testCaseId, dataSet));
    }

    private void deleteDataSet(Long id) {
        log.info("IN TestCaseRepositoryImpl deleteDataSet id={}", id);

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "deleteDataSetsByTestCaseId"),
                new MapSqlParameterSource().addValue("test_case_id", id));

    }

}
