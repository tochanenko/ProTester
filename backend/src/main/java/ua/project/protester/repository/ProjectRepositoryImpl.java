package ua.project.protester.repository;

import org.springframework.stereotype.Repository;
import ua.project.protester.model.Project;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.utils.Pagination;

import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository{
    @Override
    public void create(Project project) {

    }

    @Override
    public void update(Project project) {

    }

    @Override
    public void changeProjectStatus(Long id, Boolean isActive) {

    }

    @Override
    public List<ProjectDto> findAll(Pagination pagination) {
        return null;
    }

    @Override
    public List<ProjectDto> findAllFilteredByStatus(Pagination pagination) {
        return null;
    }

    @Override
    public Optional<Project> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Long getCountOfAllProjects() {
        return null;
    }

    @Override
    public Optional<Project> findProjectByWebsiteLink(String link) {
        return Optional.empty();
    }

    @Override
    public Optional<Project> findProjectByName(String name) {
        return Optional.empty();
    }
}
