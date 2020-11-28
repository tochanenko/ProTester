package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.ProjectAlreadyExistsException;
import ua.project.protester.exception.ProjectNotFoundException;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.service.ProjectService;
import ua.project.protester.utils.Pagination;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@RequestBody ProjectDto project) throws ProjectAlreadyExistsException {
        return projectService.createProject(project);
    }

    @PutMapping("/update")
    public ProjectDto updateProject(@RequestBody ProjectDto project) throws ProjectAlreadyExistsException {
        return projectService.updateProject(project);
    }

    @PutMapping("/changeStatus/{id}")
    public void changeProjectStatus(@PathVariable Long id) throws ProjectNotFoundException {
        projectService.changeProjectStatus(id);
    }

    @GetMapping
    public List<ProjectDto> getAllProjects(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                           @RequestParam(value = "projectActive", required = false) Boolean projectActive,
                                           @RequestParam(value = "projectName", defaultValue = "") String projectName) {

        Pagination pagination = new Pagination(pageSize, pageNumber, projectActive, projectName);

        return projectService.findAllProjects(pagination);
    }

    @GetMapping("/countOfProjects")
    public Long getCountOfAllProjects() {
        return projectService.getCountOfAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectDto getProjectById(@PathVariable Long id) throws ProjectNotFoundException {
        return projectService.getProjectDtoById(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOrderCreateException(Exception ex) {
        return new ResponseEntity<>(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
