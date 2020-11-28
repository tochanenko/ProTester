package ua.project.protester.service;

import ua.project.protester.exception.ProjectAlreadyExistsException;
import ua.project.protester.exception.ProjectNotFoundException;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.utils.Pagination;

import java.util.List;

public interface ProjectService {

    ProjectDto createProject(ProjectDto projectDto) throws ProjectAlreadyExistsException;

    ProjectDto updateProject(ProjectDto projectDto) throws ProjectAlreadyExistsException;

    void changeProjectStatus(Long projectId) throws ProjectNotFoundException;

    List<ProjectDto> findAllProjects(Pagination pagination);

    Long getCountOfAllProjects();

    ProjectDto getProjectDtoById(Long id) throws ProjectNotFoundException;

}
