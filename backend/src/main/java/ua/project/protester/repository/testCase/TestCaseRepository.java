package ua.project.protester.repository.testCase;

import ua.project.protester.exception.TestCaseCreateException;
import ua.project.protester.model.TestCase;
import ua.project.protester.response.LightTestCaseResponse;
import ua.project.protester.utils.Pagination;

import java.util.List;
import java.util.Optional;

public interface TestCaseRepository {

    TestCase create(TestCase testCase, List<Long> dataSet) throws TestCaseCreateException;

    TestCase update(TestCase testCase, List<Long> dataSet);

    void delete(Long id);

    Optional<TestCase> findById(Long id);

    List<TestCase> findAllProjectTestCases(Pagination pagination, Long projectId);

    Long getCountTestCase(Pagination pagination, Long projectId);

    Optional<TestCase> findProjectTestCase(Long projectId, Long testCaseId);

    List<LightTestCaseResponse> findTestCasesByTestScenarioId(int id);
}
