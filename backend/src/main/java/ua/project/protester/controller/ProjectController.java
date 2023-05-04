package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.ProjectAlreadyExistsException;
import ua.project.protester.exception.ProjectNotFoundException;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.service.project.ProjectService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto create(@RequestBody ProjectDto project) throws ProjectAlreadyExistsException {
        return projectService.create(project);
    }

    @PutMapping
    public ProjectDto update(@RequestBody ProjectDto project) throws ProjectAlreadyExistsException {
        return projectService.update(project);
    }

    @PutMapping("/changeStatus/{id}")
    public ProjectDto changeStatus(@PathVariable Long id) throws ProjectNotFoundException {
        return projectService.changeStatus(id);
    }

    @GetMapping
    public Page<ProjectDto> getAll(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                   @RequestParam(value = "projectName", defaultValue = "") String projectName) {

        Pagination pagination = new Pagination(pageSize, pageNumber, projectName);

        return projectService.findAll(pagination);
    }

    @GetMapping("/filter")
    public Page<ProjectDto> getAllByStatus(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                           @RequestParam(value = "projectActive") Boolean projectActive,
                                           @RequestParam(value = "projectName", defaultValue = "") String projectName) {

        Pagination pagination = new Pagination(pageSize, pageNumber, projectName);

        return projectService.findAllByStatus(pagination, projectActive);
    }

    @GetMapping("/{id}")
    public ProjectDto getById(@PathVariable Long id) throws ProjectNotFoundException {
        return projectService.getById(id);
    }

}
