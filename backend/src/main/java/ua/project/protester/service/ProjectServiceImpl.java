package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.ProjectAlreadyExistsException;
import ua.project.protester.exception.ProjectNotFoundException;
import ua.project.protester.model.Project;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.repository.ProjectRepository;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.ProjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Transactional
    @Override
    public ProjectDto createProject(ProjectDto projectDto) throws ProjectAlreadyExistsException {
        log.info("IN createProject");

        Project projectToSave = projectMapper.toEntity(projectDto);
        projectToSave.setProjectActive(true);

        try {
            projectRepository.create(projectToSave);
        } catch (Exception e) {
            log.error("IN createProject - project {} already exists", projectToSave);
            throw new ProjectAlreadyExistsException("project already exists", e);
        }

        log.info("IN createProject - project {} was successfully created", projectToSave);

        return projectMapper.toResponse(projectToSave);
    }

    @Transactional
    @Override
    public ProjectDto updateProject(ProjectDto projectDto) throws ProjectAlreadyExistsException {
        log.info("IN updateProject");

        Project projectToUpdate = projectMapper.toEntity(projectDto);

        try {
            projectRepository.update(projectToUpdate);
        } catch (Exception e) {
            log.error("IN updateProject - project {} already exists", projectToUpdate);
            throw new ProjectAlreadyExistsException("project already exists", e);
        }

        log.info("IN updateProject - project {} was successfully updated", projectToUpdate);
        return projectMapper.toResponse(projectToUpdate);
    }

    @Transactional
    @Override
    public void changeProjectStatus(Long projectId) throws ProjectNotFoundException {
        log.info("IN changeProjectStatus");

        Project projectToArchive = getProjectById(projectId);
        projectRepository.changeProjectStatus(projectId, !projectToArchive.getProjectActive());

        log.info("IN changeProjectStatus - project status {} was changed", projectToArchive);
    }

    @Override
    public Page<ProjectDto> findAllProjects(Pagination pagination) {
        log.info("IN findAllProjects");
        return new Page<>(
                projectRepository.findAll(pagination),
                projectRepository.getCountProjects(pagination)
        );
    }

    @Override
    public Page<ProjectDto> findAllProjectsByStatus(Pagination pagination) {
        log.info("IN findAllProjectsByStatus");
        return new Page<>(
                projectRepository.findAllByStatus(pagination),
                projectRepository.getCountProjectsByStatus(pagination)
        );
    }

    @Override
    public ProjectDto getProjectDtoById(Long id) throws ProjectNotFoundException {
        log.info("IN getProjectDtoById");

        Project project = getProjectById(id);

        log.info("IN getProjectDtoById - project with id {} was found", id);
        return projectMapper.toResponse(project);
    }

    private Project getProjectById(Long id) throws ProjectNotFoundException {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Can not find project with id=" + id));
    }
}
