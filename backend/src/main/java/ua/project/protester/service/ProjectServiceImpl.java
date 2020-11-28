package ua.project.protester.service;

import org.springframework.stereotype.Service;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.utils.Pagination;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        return null;
    }

    @Override
    public ProjectDto updateProject(ProjectDto projectDto) {
        return null;
    }

    @Override
    public void changeProjectStatus(Long projectId) {

    }

    @Override
    public List<ProjectDto> findAllProjects(Pagination pagination) {
        return null;
    }

    @Override
    public Long getCountOfAllProjects() {
        return null;
    }

    @Override
    public ProjectDto getProjectDtoById(Long id) {
        return null;
    }
}
