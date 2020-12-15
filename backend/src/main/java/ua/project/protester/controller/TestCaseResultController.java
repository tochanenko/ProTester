package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.result.TestCaseResultNotFoundException;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.request.TestCaseResultFilter;
import ua.project.protester.service.TestCaseResultService;
import ua.project.protester.utils.Page;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/test-case-results")
public class TestCaseResultController {

    private final TestCaseResultService testCaseResultService;

    @GetMapping("/{id}")
    public TestCaseResultDto getResultById(@PathVariable int id) throws TestCaseResultNotFoundException {
        return testCaseResultService.getTestCaseResultById(id);
    }

    @GetMapping
    public Page<TestCaseResultDto> getAllResults(
            @RequestBody TestCaseResultFilter filter,
            @RequestParam(value = "loadSteps", defaultValue = "true") boolean loadSteps) {
        return testCaseResultService.getAllTestCaseResults(filter, loadSteps);
    }
}
