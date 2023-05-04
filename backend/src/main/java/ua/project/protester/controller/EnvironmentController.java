package ua.project.protester.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.EnvironmentNotFoundException;
import ua.project.protester.model.Environment;
import ua.project.protester.service.EnvironmentService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/environment")
public class EnvironmentController {
    private final EnvironmentService environmentService;

    @GetMapping("/{id}")
    public Environment findById(@PathVariable Long id) {
        return environmentService.findById(id)
                .orElseThrow(() -> new EnvironmentNotFoundException("Environment not found"));
    }

    @PostMapping
    public Environment saveEnvironment(@RequestBody Environment environment) {
        return environmentService.save(environment);
    }

    @PutMapping
    public Environment updateEnvironment(@RequestBody Environment environment) {
        return environmentService.update(environment);
    }

    @DeleteMapping("/{id}")
    public void deleteEnvironment(@PathVariable Long id) {
         environmentService.delete(id);
    }

    @GetMapping("/findAllPaginated/{projectId}")
    public Page<Environment> findAllProjectTestCases(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "environmentName", defaultValue = "") String environmentName,
            @PathVariable Long projectId) {

        Pagination pagination = new Pagination(pageSize, pageNumber, environmentName);

        return environmentService.findAllPaginatedByProjectId(pagination, projectId);
    }

    @GetMapping("findAll/{projectId}")
    public List<Environment> findAll(@PathVariable Long projectId) {
        return environmentService.findAll(projectId);
    }
}
