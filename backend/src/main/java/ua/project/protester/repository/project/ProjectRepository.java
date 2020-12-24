package ua.project.protester.repository.project;

import ua.project.protester.exception.ProjectCreateException;
import ua.project.protester.model.Project;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.utils.Pagination;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    Project create(Project project) throws ProjectCreateException;

    Project update(Project project);

    Project changeProjectStatus(Project project, Boolean isActive);

    List<ProjectDto> findAll(Pagination pagination);

    List<ProjectDto> findAllByStatus(Pagination pagination, Boolean isActive);

    Optional<Project> findById(Long id);

    Long getCountProjects(Pagination pagination);

    Long getCountProjectsByStatus(Pagination pagination, Boolean isActive);

}
