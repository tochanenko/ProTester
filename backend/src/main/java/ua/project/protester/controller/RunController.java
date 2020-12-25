package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.exception.executable.compound.CompoundNotFoundException;
import ua.project.protester.exception.executable.scenario.TestScenarioNotFoundException;
import ua.project.protester.model.RunResult;
import ua.project.protester.request.RunTestCaseRequest;
import ua.project.protester.response.TestCaseResponse;
import ua.project.protester.response.ValidationDataSetResponse;
import ua.project.protester.service.StartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Slf4j
public class RunController {
    private final StartService startService;

    @PostMapping
    public RunResult save(@RequestBody RunTestCaseRequest testCase) throws TestScenarioNotFoundException, CompoundNotFoundException, OuterComponentNotFoundException {
        log.info("test case response{}", testCase);
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
