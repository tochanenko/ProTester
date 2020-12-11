package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.TestCaseResultNotFoundException;
import ua.project.protester.model.executable.result.TestCaseResult;
import ua.project.protester.service.AnalyzeTestCaseResultService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/analyze")
public class AnalyzeTestCaseResultController {

    private final AnalyzeTestCaseResultService analyzeTestCaseResultService;

    @GetMapping("/{id}")
    public TestCaseResult getResultById(@PathVariable int id) throws TestCaseResultNotFoundException {
        return analyzeTestCaseResultService.getTestCaseResultById(id);
    }
}
