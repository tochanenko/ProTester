package ua.project.protester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.environment.Environment;
import ua.project.protester.environment.EnvironmentRepository;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.exception.executable.compound.CompoundNotFoundException;
import ua.project.protester.model.TestCase;
import ua.project.protester.run.RunTestCase;

@RestController
@RequestMapping("/api/test")
public class RunController {

    @Autowired
    private EnvironmentRepository repository;

    @Autowired
    private RunTestCase testScenario;

    @GetMapping("/{id}")
    public ResponseEntity<Environment> hello(@RequestBody TestCase testCase) {
        return new ResponseEntity<>(repository.findById(1L).get(), HttpStatus.OK);
    }

    @PostMapping
    public void run() throws OuterComponentNotFoundException, CompoundNotFoundException {
        testScenario.secondRun();
    }
}
