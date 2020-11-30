package ua.project.protester.service;

import ua.project.protester.exception.ProjectAlreadyExistsException;
import ua.project.protester.exception.ProjectNotFoundException;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

public interface ProjectService {

    ProjectDto createProject(ProjectDto projectDto) throws ProjectAlreadyExistsException;

    ProjectDto updateProject(ProjectDto projectDto) throws ProjectAlreadyExistsException;

    void changeProjectStatus(Long projectId) throws ProjectNotFoundException;

    Page<ProjectDto> findAllProjects(Pagination pagination);

    Page<ProjectDto> findAllProjectsByStatus(Pagination pagination);

    ProjectDto getProjectDtoById(Long id) throws ProjectNotFoundException;

}
