package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.exception.result.TestCaseResultNotFoundException;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.service.AnalyzeTestCaseResultService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/analyze")
public class AnalyzeTestCaseResultController {

    private final AnalyzeTestCaseResultService analyzeTestCaseResultService;

    @GetMapping("/{id}")
    public TestCaseResultDto getResultById(@PathVariable int id) throws TestCaseResultNotFoundException {
        return analyzeTestCaseResultService.getTestCaseResultById(id);
    }
}
