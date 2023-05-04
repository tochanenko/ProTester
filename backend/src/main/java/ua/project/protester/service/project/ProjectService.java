package ua.project.protester.service.project;

import ua.project.protester.exception.ProjectAlreadyExistsException;
import ua.project.protester.exception.ProjectNotFoundException;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

public interface ProjectService {

    ProjectDto create(ProjectDto projectDto) throws ProjectAlreadyExistsException;

    ProjectDto update(ProjectDto projectDto) throws ProjectAlreadyExistsException;

    ProjectDto changeStatus(Long projectId) throws ProjectNotFoundException;

    Page<ProjectDto> findAll(Pagination pagination);

    Page<ProjectDto> findAllByStatus(Pagination pagination, Boolean isActive);

    ProjectDto getById(Long id) throws ProjectNotFoundException;

}
