package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.executable.OuterComponentStepSaveException;
import ua.project.protester.exception.executable.scenario.TestScenarioNotFoundException;
import ua.project.protester.exception.executable.scenario.UsedTestScenarioDeleteException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.request.OuterComponentFilter;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.service.TestScenarioService;
import ua.project.protester.utils.Page;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test-scenarios")
public class TestScenarioController {
    private final TestScenarioService testScenarioService;

    @PostMapping
    public OuterComponent createTestScenario(@RequestBody OuterComponentRepresentation request) throws OuterComponentStepSaveException {
        return testScenarioService.saveTestScenario(request);
    }

    @PutMapping("/{id}")
    public OuterComponent updateTestScenario(@RequestBody OuterComponentRepresentation request, @PathVariable int id) throws OuterComponentStepSaveException {
        return testScenarioService.updateTestScenario(id, request);
    }

    @GetMapping
    public Page<OuterComponent> getAllTestScenarios(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                    @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                    @RequestParam(value = "scenarioName", defaultValue = "") String scenarioName,
                                                    @RequestParam(value = "loadSteps", defaultValue = "true") boolean loadSteps) {
        OuterComponentFilter filter = new OuterComponentFilter(pageSize, pageNumber, scenarioName);
        return testScenarioService.getAllTestScenarios(filter, loadSteps);
    }

    @GetMapping("/{id}")
    public OuterComponent getTestScenario(@PathVariable int id) throws TestScenarioNotFoundException {
        return testScenarioService.getTestScenarioById(id);
    }

    @DeleteMapping("/{id}")
    public OuterComponent deleteTestScenario(@PathVariable int id) throws UsedTestScenarioDeleteException {
        return testScenarioService.deleteTestScenarioById(id);
    }
}
