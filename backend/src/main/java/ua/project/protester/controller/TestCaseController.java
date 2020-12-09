package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.TestCaseNotFoundException;
import ua.project.protester.request.TestCaseRequest;
import ua.project.protester.response.TestCaseResponse;
import ua.project.protester.service.testcase.TestCaseService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

//@PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ENGINEER')")
@RestController
@RequestMapping("/api/testCase")
@RequiredArgsConstructor
public class TestCaseController {

    private final TestCaseService testCaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TestCaseResponse createTestCase(@RequestBody TestCaseRequest testCase) {
        return testCaseService.create(testCase);
    }

    @PutMapping
    public TestCaseResponse updateTestCase(@RequestBody TestCaseRequest testCase) {
        return testCaseService.update(testCase);
    }

    @DeleteMapping("/{id}")
    public String deleteTestCase(@PathVariable Long id) throws TestCaseNotFoundException {
        testCaseService.delete(id);
        return "deleted";
    }

    @GetMapping("/{id}")
    public TestCaseResponse findTestCaseById(@PathVariable Long id) throws TestCaseNotFoundException {
        return testCaseService.findById(id);
    }

    @GetMapping("/project/{projectId}")
    public Page<TestCaseResponse> findAllProjectTestCases(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "testCaseName", defaultValue = "") String testCaseName,
            @PathVariable Long projectId) {

        Pagination pagination = new Pagination(pageSize, pageNumber, testCaseName);

        return testCaseService.findAllProjectTestCases(pagination, projectId);
    }

}
