package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.exception.executable.TestScenarioNotFoundException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.result.AbstractActionResult;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/analyze")
public class AnalyzeTestCaseResultController {

    private final TestScenarioController testScenarioController;

    @GetMapping("/{id}")
    public void test(@PathVariable int id) throws TestScenarioNotFoundException {
        OuterComponent t = testScenarioController.getTestScenario(id);
        List<AbstractActionResult> results =
                t.executeForResult(
                        Map.of("a", "aVal", "b", "bVal", "c", "cVal"),
                        null,
                        System.out::println);
        System.out.println(results);
    }
}
