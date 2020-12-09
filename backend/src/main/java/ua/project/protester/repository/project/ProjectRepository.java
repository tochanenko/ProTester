package ua.project.protester.repository.project;

import ua.project.protester.model.Project;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.utils.Pagination;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    void create(Project project);

    void update(Project project);

    void changeProjectStatus(Long id, Boolean isActive);

    List<ProjectDto> findAll(Pagination pagination);

    List<ProjectDto> findAllByStatus(Pagination pagination, Boolean isActive);

    Optional<Project> findById(Long id);

    Long getCountProjects(Pagination pagination);

    Long getCountProjectsByStatus(Pagination pagination, Boolean isActive);

}
