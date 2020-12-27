package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.TestCaseCreateException;
import ua.project.protester.exception.TestCaseNotFoundException;
import ua.project.protester.exception.executable.scenario.TestScenarioNotFoundException;
import ua.project.protester.model.TestCaseDto;
import ua.project.protester.service.testcase.TestCaseService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/testCase")
@RequiredArgsConstructor
public class TestCaseController {

    private final TestCaseService testCaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TestCaseDto createTestCase(@RequestBody TestCaseDto testCase) throws TestCaseCreateException {
        return testCaseService.create(testCase);
    }

    @PutMapping
    public TestCaseDto updateTestCase(@RequestBody TestCaseDto testCase) {
        return testCaseService.update(testCase);
    }

    @DeleteMapping("/{id}")
    public void deleteTestCase(@PathVariable Long id) throws TestCaseNotFoundException {
        testCaseService.delete(id);
    }

    @GetMapping("/{id}")
    public TestCaseDto findTestCaseById(@PathVariable Long id) throws TestCaseNotFoundException {
        return testCaseService.findById(id);
    }

    @GetMapping("/project/{projectId}")
    public Page<TestCaseDto> findAllProjectTestCases(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "testCaseName", defaultValue = "") String testCaseName,
            @PathVariable Long projectId) {

        Pagination pagination = new Pagination(pageSize, pageNumber, testCaseName);

        return testCaseService.findAllProjectTestCases(pagination, projectId);
    }


    @GetMapping("/environment/{scenarioId}")
    public boolean findSqlActions(@PathVariable Integer scenarioId) throws TestCaseNotFoundException, TestScenarioNotFoundException {

        return testCaseService.findSqlActionsInTestCaseByProjectIdAndTestCaseId(scenarioId);
    }

}
