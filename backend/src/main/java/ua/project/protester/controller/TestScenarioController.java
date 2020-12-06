package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.executable.TestScenarioNotFoundException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.request.BaseFilter;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.service.TestScenarioService;
import ua.project.protester.utils.Page;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test-scenarios")
public class TestScenarioController {
    private final TestScenarioService testScenarioService;

    @PostMapping
    public void createTestScenario(@RequestBody OuterComponentRepresentation request) {
        testScenarioService.saveTestScenario(request);
    }

    @PutMapping("/{id}")
    public void updateTestScenario(@RequestBody OuterComponentRepresentation request, @PathVariable int id) throws TestScenarioNotFoundException {
        testScenarioService.updateTestScenario(id, request);
    }

    @GetMapping
    public Page<OuterComponent> getAllTestScenarios(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                    @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                    @RequestParam(value = "scenarioName", defaultValue = "") String scenarioName) {
        BaseFilter filter = new BaseFilter(pageSize, pageNumber, scenarioName);
        return testScenarioService.getAllTestScenarios(filter);
    }

    @GetMapping("/{id}")
    public OuterComponent getTestScenario(@PathVariable int id) throws TestScenarioNotFoundException {
        return testScenarioService.getTestScenarioById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTestScenario(@PathVariable int id) {
        testScenarioService.deleteTestScenarioById(id);
    }
}
