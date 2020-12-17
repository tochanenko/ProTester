package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.RunResult;
import ua.project.protester.request.RunTestCaseRequest;
import ua.project.protester.service.StartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class RunController {
    private final StartService startService;

    @PostMapping
    public RunResult save(@RequestBody RunTestCaseRequest testCase)  {
       return startService.getTestCaseExecutionResult(testCase);
    }

    @Transactional
    @GetMapping("/{id}")
    public void run(@PathVariable Long id) {
        startService.execute(id);
    }

}
