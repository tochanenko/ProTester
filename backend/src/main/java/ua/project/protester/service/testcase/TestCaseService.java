package ua.project.protester.service.testcase;

import ua.project.protester.exception.TestCaseCreateException;
import ua.project.protester.exception.TestCaseNotFoundException;
import ua.project.protester.exception.executable.scenario.TestScenarioNotFoundException;
import ua.project.protester.model.TestCaseDto;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

public interface TestCaseService {

    TestCaseDto create(TestCaseDto testCaseDto) throws TestCaseCreateException;

    TestCaseDto update(TestCaseDto testCaseDto);

    void delete(Long id) throws TestCaseNotFoundException;

    TestCaseDto findById(Long id) throws TestCaseNotFoundException;

    Page<TestCaseDto> findAllProjectTestCases(Pagination pagination, Long projectId);

    boolean findSqlActionsInTestCaseByProjectIdAndTestCaseId(Long projectId, Long testCaseId) throws TestCaseNotFoundException, TestScenarioNotFoundException;

}
