package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.result.TestCaseResultNotFoundException;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.service.TestCaseResultService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

//@PreAuthorize("isAuthenticated()")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/test-case-results")
public class TestCaseResultController {

    private final TestCaseResultService testCaseResultService;

    @GetMapping("/{id}")
    public TestCaseResultDto getResultById(@PathVariable int id) throws TestCaseResultNotFoundException {
        return testCaseResultService.getTestCaseResultById(id);
    }

    @GetMapping("/project/{projectId}")
    public Page<TestCaseResultDto> findAllProjectTestCases(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "projectName", defaultValue = "") String projectName,
            @PathVariable Long projectId) {

        Pagination pagination = new Pagination(pageSize, pageNumber, projectName);

        return testCaseResultService.findAllResultsByProject(pagination, projectId);
    }
}
