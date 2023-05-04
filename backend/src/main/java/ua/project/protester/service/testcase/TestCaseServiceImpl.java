package ua.project.protester.service.testcase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.TestCaseCreateException;
import ua.project.protester.exception.TestCaseNotFoundException;
import ua.project.protester.exception.executable.scenario.TestScenarioNotFoundException;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.TestCaseDto;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.Step;
import ua.project.protester.repository.OuterComponentRepository;
import ua.project.protester.repository.testCase.TestCaseRepository;
import ua.project.protester.service.TestScenarioService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.testcase.TestCaseMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper testCaseMapper;
    private final TestScenarioService scenarioService;
    private final OuterComponentRepository outerComponentRepository;

    @Transactional
    @Override
    public TestCaseDto create(TestCaseDto testCaseDto) throws TestCaseCreateException {
        log.info("IN TestCaseServiceImpl create - testCase, {}", testCaseDto);

        TestCase createdTestCase = testCaseRepository.create(testCaseMapper.toEntity(testCaseDto));

        log.info("IN TestCaseServiceImpl create - testCase: {} was successfully created", createdTestCase);
        return testCaseMapper.toResponse(createdTestCase);
    }

    @Transactional
    @Override
    public TestCaseDto update(TestCaseDto testCaseDto) {
        log.info("IN TestCaseServiceImpl update - testCase, {}", testCaseDto);

        TestCase updatedTestCase = testCaseRepository.update(testCaseMapper.toEntity(testCaseDto));

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
    public TestCaseDto findById(Long id) throws TestCaseNotFoundException {
        log.info("IN TestCaseServiceImpl findById - id={}", id);

        TestCase testCase = getTestCaseById(id);

        log.info("IN  TestCaseServiceImpl findById, id={}, found {}", id, testCase);
        return testCaseMapper.toResponse(testCase);
    }


    @Override
    public Page<TestCaseDto> findAllProjectTestCases(Pagination pagination, Long projectId) {
        log.info("IN TestCaseServiceImpl findAllProjectTestCases - pagination={}, projectId={}", pagination, projectId);

        List<TestCaseDto> testCaseList = testCaseRepository.findAllProjectTestCases(pagination, projectId).stream()
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


    public boolean findSqlActionsInTestCaseByProjectIdAndTestCaseId(Integer scenarioId) throws TestScenarioNotFoundException {
        return findStepsRecursively(scenarioService.getTestScenarioById(scenarioId).getSteps()
                .stream())
                .collect(Collectors.toList())
                .stream()
                .filter(Step::isAction)
                .anyMatch(step -> step.getComponent().getType().equals(ExecutableComponentType.SQL));
    }

    private Stream<Step> findStepsRecursively(Stream<Step> initial) {
        return initial
                .flatMap(s -> {
                    if (s.isAction()) {
                        return Stream.of(s);
                    } else {
                        return findStepsRecursively(outerComponentRepository.findOuterComponentById(s.getComponent().getId(), true).getSteps().stream());
                    }
                });
    }
}
