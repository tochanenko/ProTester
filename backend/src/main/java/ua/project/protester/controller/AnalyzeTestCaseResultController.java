package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/analyze")
public class AnalyzeTestCaseResultController {

    @GetMapping("/{id}")
    public void test(@PathVariable int id) {
        // OuterComponent t = testScenarioController.getTestScenario(id);
        //t.execute(
        //        Map.of("a", "aVal", "b", "bVal", "c", "cVal"),
        //        null,
        //        System.out::println);
    }
}
