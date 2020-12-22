package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.executable.TestScenarioNotFoundException;
import ua.project.protester.model.RunResult;
import ua.project.protester.request.RunTestCaseRequest;
import ua.project.protester.response.TestCaseResponse;
import ua.project.protester.response.ValidationDataSetResponse;
import ua.project.protester.service.StartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class RunController {
    private final StartService startService;

    @PostMapping
    public RunResult save(@RequestBody RunTestCaseRequest testCase) throws TestScenarioNotFoundException {
       return startService.getTestCaseExecutionResult(testCase);
    }

    @Transactional
    @GetMapping("/{id}")
    public void run(@PathVariable Long id) throws TestScenarioNotFoundException {
        startService.execute(id);
    }

    @GetMapping("/result/{id}")
    public RunResult findResultById(@PathVariable Long id) {
        return startService.findById(id);
    }

    @PostMapping("/validate")
    public ValidationDataSetResponse validate(@RequestBody TestCaseResponse testCaseResponse) throws TestScenarioNotFoundException {
        return startService.validateDataSetWithTestScenario(testCaseResponse);
    }
}
