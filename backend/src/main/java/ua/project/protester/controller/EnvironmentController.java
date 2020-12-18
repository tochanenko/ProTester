package ua.project.protester.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.Environment;
import ua.project.protester.service.EnvironmentService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/environment")
public class EnvironmentController {
    private final EnvironmentService environmentService;

    @GetMapping
    public List<Environment> findAll() {
        return environmentService.findAll();
    }

    @GetMapping("/{id}")
    public Environment findById(@PathVariable Long id) {
        return environmentService.findById(id).get();
    }

    @PostMapping
    public Environment saveEnvironment(Environment environment) {
        return environmentService.save(environment);
    }

    @PutMapping
    public Environment updateEnvironment(Environment environment) {
        return environmentService.update(environment);
    }

    @DeleteMapping
    public void deleteEnvironment(Long id) {
         environmentService.delete(id);
    }
}
