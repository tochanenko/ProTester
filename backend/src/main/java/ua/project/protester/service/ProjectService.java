package ua.project.protester.service;

import ua.project.protester.model.ProjectDto;
import ua.project.protester.utils.Pagination;

import java.util.List;

public interface ProjectService {

    ProjectDto createProject(ProjectDto projectDto);

    ProjectDto updateProject(ProjectDto projectDto);

    void changeProjectStatus(Long projectId);

    List<ProjectDto> findAllProjects(Pagination pagination);

    Long getCountOfAllProjects();

    ProjectDto getProjectDtoById(Long id);

}
