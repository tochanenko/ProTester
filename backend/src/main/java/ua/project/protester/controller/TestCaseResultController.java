package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.result.TestCaseResultNotFoundException;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.service.TestCaseResultService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/test-case-results")
public class TestCaseResultController {

    private final TestCaseResultService testCaseResultService;

    @GetMapping("/{id}")
    public TestCaseResultDto getResultById(@PathVariable int id) throws TestCaseResultNotFoundException {
        return testCaseResultService.getTestCaseResultById(id);
    }
}
