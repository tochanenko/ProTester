package ua.project.protester.service.testcase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.TestCaseCreateException;
import ua.project.protester.exception.TestCaseNotFoundException;
import ua.project.protester.exception.executable.scenario.TestScenarioNotFoundException;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.Step;
import ua.project.protester.repository.testCase.TestCaseRepository;
import ua.project.protester.request.TestCaseRequest;
import ua.project.protester.response.TestCaseResponse;
import ua.project.protester.service.TestScenarioService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.testcase.TestCaseMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper testCaseMapper;
    private final TestScenarioService scenarioService;

    @Transactional
    @Override
    public TestCaseResponse create(TestCaseRequest testCaseRequest) throws TestCaseCreateException {
        log.info("IN TestCaseServiceImpl create - testCase, {}", testCaseRequest);

        TestCase createdTestCase = testCaseRepository.create(
                testCaseMapper.toEntity(testCaseRequest),
                testCaseRequest.getDataSetId());

        log.info("IN TestCaseServiceImpl create - testCase: {} was successfully created", createdTestCase);
        return testCaseMapper.toResponse(createdTestCase);
    }

    @Transactional
    @Override
    public TestCaseResponse update(TestCaseRequest testCaseRequest) {
        log.info("IN TestCaseServiceImpl update - testCase, {}", testCaseRequest);

        TestCase updatedTestCase = testCaseRepository.update(
                testCaseMapper.toEntity(testCaseRequest),
                testCaseRequest.getDataSetId());

        log.info("IN TestCaseServiceImpl update - testCase: {} was successfully updated", updatedTestCase);
        return testCaseMapper.toResponse(updatedTestCase);
    }

    @Transactional
    @Override
    public void delete(Long id) throws TestCaseNotFoundException {
        log.info("IN TestCaseServiceImpl delete - testCase, id={}", id);

        TestCase testCase = getTestCaseById(id);

        testCaseRepository.delete(testCase.getId());
        log.info("IN TestCaseServiceImpl delete - testCase id={} was successfully deleted", id);
    }

    @Override
    public TestCaseResponse findById(Long id) throws TestCaseNotFoundException {
        log.info("IN TestCaseServiceImpl findById - id={}", id);

        TestCase testCase = getTestCaseById(id);

        log.info("IN  TestCaseServiceImpl findById, id={}, found {}", id, testCase);
        return testCaseMapper.toResponse(testCase);
    }


    @Override
    public Page<TestCaseResponse> findAllProjectTestCases(Pagination pagination, Long projectId) {
        log.info("IN TestCaseServiceImpl findAllProjectTestCases - pagination={}, projectId={}", pagination, projectId);

        List<TestCaseResponse> testCaseList = testCaseRepository.findAllProjectTestCases(pagination, projectId).stream()
                .map(testCaseMapper::toResponse)
                .collect(Collectors.toList());

        return new Page<>(
                testCaseList,
                testCaseRepository.getCountTestCase(pagination, projectId)
        );
    }

    private TestCase getTestCaseById(Long id) throws TestCaseNotFoundException {
        return testCaseRepository.findById(id)
                .orElseThrow(TestCaseNotFoundException::new);
    }


    public boolean findSqlActionsInTestCaseByProjectIdAndTestCaseId(Long projectId, Long testCaseId) throws TestCaseNotFoundException, TestScenarioNotFoundException {
        return scenarioService.getTestScenarioById(testCaseRepository.findProjectTestCase(projectId, testCaseId)
                .orElseThrow(TestCaseNotFoundException::new).getScenarioId().intValue())
                .getSteps()
                .stream()
                .filter(Objects::nonNull)
                .filter(Step::isAction)
                .anyMatch(step -> step.getComponent().getType().equals(ExecutableComponentType.SQL));
    }
}
