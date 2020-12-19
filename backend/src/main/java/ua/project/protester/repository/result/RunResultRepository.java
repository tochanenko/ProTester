package ua.project.protester.repository.result;

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
import ua.project.protester.exception.executable.TestScenarioNotFoundException;
import ua.project.protester.model.ActionWrapper;
import ua.project.protester.model.RunResult;
import ua.project.protester.model.TestCaseWrapperResult;
import ua.project.protester.model.executable.Step;
import ua.project.protester.model.executable.result.ResultStatus;
import ua.project.protester.response.TestCaseResponse;
import ua.project.protester.service.TestScenarioService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.project.protester.utils.PropertyExtractor.extract;


@Repository
@RequiredArgsConstructor
@PropertySource("classpath:queries/run-result.properties")
@Slf4j
public class RunResultRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final TestScenarioService testScenarioService;

    public TestCaseWrapperResult saveTestCaseWrapperResult(TestCaseResponse testCaseResponse, Integer runResultId, Integer testCaseResultId) {
        TestCaseWrapperResult wrapperResult = new TestCaseWrapperResult();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("saving  test case wrapper result with scenarioId {}", testCaseResponse.getScenarioId());

        namedParameterJdbcTemplate.update(
                extract(env, "saveTestCaseWrapper"),
                new MapSqlParameterSource()
                        .addValue("scenario_id", testCaseResponse.getScenarioId())
                        .addValue("run_result_id", runResultId)
                        .addValue("test_case_result", testCaseResultId),
                keyHolder,
                new String[]{"case_wrapper_id"});
        Integer id = (Integer) keyHolder.getKey();

        wrapperResult.setTestResultId(testCaseResultId);
        wrapperResult.setId(id);
        return wrapperResult;
    }

    public List<ActionWrapper> saveActionWrappersByTestCaseResultWrapperId(Integer testCaseWrapperResultId, List<Step> step) {
           return step.stream()
                   .map(step1 -> saveActionWrapperWithStep(testCaseWrapperResultId, step1))
                   .collect(Collectors.toList());
    }

    private ActionWrapper saveActionWrapperWithStep(Integer testCaseResultWrapperId, Step step) {
        ActionWrapper wrapperResult = new ActionWrapper();
        wrapperResult.setResultStatus(ResultStatus.IN_PROGRESS);
        wrapperResult.setName(step.getComponent().getName());
        wrapperResult.setDescription(step.getComponent().getDescription());
        wrapperResult.setType(step.getComponent().getType());
        wrapperResult.setParameters(step.getParameters());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("saving  action wrapper result with test case wrapper result id {}", testCaseResultWrapperId);

        namedParameterJdbcTemplate.update(
                extract(env, "saveActionWrapper"),
                new MapSqlParameterSource()
                        .addValue("test_case_wrapper_id", testCaseResultWrapperId)
                        .addValue("step_id", step.getId()),
                keyHolder,
                new String[]{"action_wrapper_id"});
        Integer id = (Integer) keyHolder.getKey();

        wrapperResult.setId(id);
        return wrapperResult;
    }

    public RunResult saveRunResult(Long userId) {
        RunResult runResult = new RunResult();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                extract(env, "saveRunResult"),
                new MapSqlParameterSource()
                        .addValue("user_id", userId),
                keyHolder,
                new String[]{"run_id"});
        Integer id = (Integer) keyHolder.getKey();

        runResult.setId(id.longValue());
        return runResult;
    }

    public List<ActionWrapper> findActionWrapperByTestCaseWrapperResult(Integer testCaseWrapperId, Integer scenarioId) throws TestScenarioNotFoundException {
        List<Step> steps = testScenarioService.getTestScenarioById(scenarioId).getSteps();

        try {
            List<ActionWrapper> actionWrapperList = namedParameterJdbcTemplate.query(
                    extract(env, "findActionWrapperResultsByTestCaseWrapperId"),
                    new MapSqlParameterSource()
                            .addValue("case_wrapper_id", testCaseWrapperId),
                    (rs, rowNum) -> new ActionWrapper(
                            rs.getInt("action_wrapper_id"),
                            rs.getInt("step_id"))
            );
            if (actionWrapperList.size() == 0) {
                return Collections.emptyList();
            }
            return actionWrapperList.stream()
                    .map(actionWrapper -> connectActionWrapperWithStep(actionWrapper.getId(), steps.stream()
                            .filter(step -> step.getId().equals(actionWrapper.getStepId()))
                            .findFirst().get())
                            .get())
                    .collect(Collectors.toList());
            } catch (EmptyResultDataAccessException e) {
            log.warn("action wrappers were not found");
            return Collections.emptyList();
        }
    }

    public int findScenarioIdByTestCaseWrapperResult(Integer testCaseResultId) {

        try {
            TestCaseWrapperResult testCaseWrapper = namedParameterJdbcTemplate.queryForObject(
                    extract(env, "findScenarioIdByTestCaseWrapper"),
                    new MapSqlParameterSource()
                            .addValue("test_case_result", testCaseResultId),
                    (rs, rowNum) -> new TestCaseWrapperResult(
                            rs.getInt("case_wrapper_id"),
                            rs.getInt("scenario_id"),
                            rs.getInt("test_case_result"))
            );
            if (testCaseWrapper == null) {
                throw new EmptyResultDataAccessException(0);
            }
            return  testCaseWrapper.getScenarioId();
        } catch (EmptyResultDataAccessException e) {
            log.warn("test case wrapper were not found");
            return 0;
        }
    }

    private Optional<ActionWrapper> connectActionWrapperWithStep(Integer actionWrapperId, Step step) {

        try {
            ActionWrapper actionWrapper = namedParameterJdbcTemplate.queryForObject(
                    extract(env, "findActionWrapperById"),
                    new MapSqlParameterSource().addValue("action_wrapper_id", actionWrapperId),
                    (rs, rowNum) -> new ActionWrapper(
                            rs.getInt("action_wrapper_id"),
                            rs.getInt("step_id")));

            if (actionWrapper == null) {
                return Optional.empty();
            }
            actionWrapper.setResultStatus(ResultStatus.IN_PROGRESS);
            actionWrapper.setName(step.getComponent().getName());
            actionWrapper.setDescription(step.getComponent().getDescription());
            actionWrapper.setType(step.getComponent().getType());
            actionWrapper.setParameters(step.getParameters());
            return Optional.of(actionWrapper);
        } catch (EmptyResultDataAccessException e) {
            log.warn("action wrapper with id {} was`nt found", actionWrapperId);
            return Optional.empty();
        }
    }

    private List<TestCaseWrapperResult> findTestCaseWrapperResultsByRunId(Long runResultId) {
        try {
            List<TestCaseWrapperResult> testCaseWrapperResults = namedParameterJdbcTemplate.query(
                    extract(env, "findTestCaseWrapperResultsByRunResultId"),
                    new MapSqlParameterSource()
                            .addValue("run_result_id", runResultId),
            (rs, rowNum) -> new TestCaseWrapperResult(
                            rs.getInt("case_wrapper_id"),
                            rs.getInt("scenario_id"),
                            rs.getInt("test_case_result"))
            );
            if (testCaseWrapperResults.size() == 0) {
                return Collections.emptyList();
            }
            testCaseWrapperResults.forEach(result -> {
                try {
                    result.setActionWrapperList(
                            findActionWrapperByTestCaseWrapperResult(result.getId(), result.getScenarioId())
                    );
                } catch (TestScenarioNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return testCaseWrapperResults;
        } catch (EmptyResultDataAccessException e) {
            log.warn("test case wrapper results  were`nt found");
            return Collections.emptyList();
        }
    }

    public Optional<RunResult> findRunResultById(Long id) {
        try {
            RunResult runResult = namedParameterJdbcTemplate.queryForObject(
                    extract(env, "findRunResultById"),
                    new MapSqlParameterSource().addValue("run_id", id),
                    (rs, rowNum) -> new RunResult(
                            rs.getLong("run_id"))
            );
            if (runResult == null) {
                return Optional.empty();
            }
            runResult.setTestCaseResults(findTestCaseWrapperResultsByRunId(id));
            return Optional.of(runResult);
        } catch (EmptyResultDataAccessException  e) {
            log.warn("run result with id {} was`nt found", id);
            return Optional.empty();
        }
    }

}
