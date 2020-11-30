package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.ProjectAlreadyExistsException;
import ua.project.protester.exception.ProjectNotFoundException;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.service.ProjectService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

@PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ENGINEER')")
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
    public Page<ProjectDto> getAllProjects(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                           @RequestParam(value = "projectName", defaultValue = "") String projectName) {

        Pagination pagination = new Pagination(pageSize, pageNumber, projectName);

        return projectService.findAllProjects(pagination);
    }

    @GetMapping("/filter")
    public Page<ProjectDto> getAllProjectsByStatus(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                   @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                   @RequestParam(value = "projectActive") Boolean projectActive,
                                                   @RequestParam(value = "projectName", defaultValue = "") String projectName) {

        Pagination pagination = new Pagination(pageSize, pageNumber, projectActive, projectName);

        return projectService.findAllProjectsByStatus(pagination);
    }

    @GetMapping("/{id}")
    public ProjectDto getProjectById(@PathVariable Long id) throws ProjectNotFoundException {
        return projectService.getProjectDtoById(id);
    }

}
