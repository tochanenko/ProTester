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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.result.TestCaseResultNotFoundException;
import ua.project.protester.model.executable.result.ResultStatus;
import ua.project.protester.model.executable.result.TestCaseResult;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.repository.StatusRepository;
import ua.project.protester.repository.UserRepository;
import ua.project.protester.repository.testCase.TestCaseRepository;
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.PropertyExtractor;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@PropertySource("classpath:queries/test-case-result.properties")
@Repository
@RequiredArgsConstructor
@Slf4j
public class TestCaseResultRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final ActionResultRepository actionResultRepository;
    private final TestCaseRepository testCaseRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    public TestCaseResultDto save(TestCaseResultDto result) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveTestCaseResult"),
                new BeanPropertySqlParameterSource(getModelFromDto(result)),
                keyHolder,
                new String[]{"test_case_result_id"});
        result.setId((Integer) keyHolder.getKey());
        return result;
    }

    public int updateStatusAndEndDate(Integer id, ResultStatus status, OffsetDateTime endDate) {
        return namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateTestCaseResultStatusAndEndDate"),
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("statusId", statusRepository.getIdByLabel(status))
                        .addValue("endDate", endDate));
    }

    public List<TestCaseResultDto> findAllByProjectId(Pagination pagination, Long projectId) {
        List<TestCaseResult> results = namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findTestCaseResultsByProjectId"),
                new MapSqlParameterSource()
                        .addValue("pageSize", pagination.getPageSize())
                        .addValue("offset", pagination.getOffset())
                        .addValue("filterName", pagination.getSearchField() + "%")
                        .addValue("projectId", projectId),
                new BeanPropertyRowMapper<>(TestCaseResult.class));

        if (results.size() > 0) {
            return results
                    .stream()
                    .map(this::getDtoFromModel)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public List<TestCaseResultDto> findAllProjectsResult(Pagination pagination) {
        List<TestCaseResult> results = namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findTestCaseResultsForAllProjects"),
                new MapSqlParameterSource()
                        .addValue("pageSize", pagination.getPageSize())
                        .addValue("offset", pagination.getOffset()),
                new BeanPropertyRowMapper<>(TestCaseResult.class));

        if (results.size() > 0) {
            return results
                    .stream()
                    .map(this::getDtoFromModel)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Long countAllTestCases(Pagination pagination) {
        return namedParameterJdbcTemplate.queryForObject(
                PropertyExtractor.extract(env, "countAllTestCasesResult"),
                new MapSqlParameterSource()
                        .addValue("pageSize", pagination.getPageSize())
                        .addValue("offset", pagination.getOffset()),
                Long.class);
    }

    public Long countTestCaseResult(Pagination pagination, Long projectId) {

        return namedParameterJdbcTemplate.queryForObject(
                PropertyExtractor.extract(env, "countTestCaseResult"),
                new MapSqlParameterSource()
                        .addValue("filterName", pagination.getSearchField() + "%")
                        .addValue("projectId", projectId)
                        .addValue("pageSize", pagination.getPageSize())
                        .addValue("offset", pagination.getOffset()),
                Long.class);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public TestCaseResultDto findById(Integer id) throws TestCaseResultNotFoundException {
        try {
            TestCaseResult result = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findTestCaseResultById"),
                    new MapSqlParameterSource()
                            .addValue("id", id),
                    new BeanPropertyRowMapper<>(TestCaseResult.class));
            if (result == null) {
                throw new TestCaseResultNotFoundException(id);
            }
            return getDtoFromModel(result);
        } catch (DataAccessException e) {
            log.warn(e.getMessage(), e);
            throw new TestCaseResultNotFoundException(id, e);
        }
    }

    private TestCaseResult getModelFromDto(TestCaseResultDto dto) {
        return new TestCaseResult(
                null,
                dto.getUser() != null ? dto.getUser().getId() : null,
                dto.getTestCase() != null ? dto.getTestCase().getId() : null,
                statusRepository.getIdByLabel(dto.getStatus()),
                dto.getStartDate(),
                dto.getEndDate());
    }

    private TestCaseResultDto getDtoFromModel(TestCaseResult result) {
        return new TestCaseResultDto(
                result.getId(),
                userRepository.findById(result.getUserId()).orElse(null),
                testCaseRepository.findById(result.getTestCaseId()).orElse(null),
                statusRepository.getLabelById(result.getStatusId()),
                result.getStartDate(),
                result.getEndDate(),
                actionResultRepository.findByTestCaseResultId(result.getId()));
    }
}
