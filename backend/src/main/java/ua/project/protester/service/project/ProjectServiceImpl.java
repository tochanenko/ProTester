package ua.project.protester.service.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.ProjectAlreadyExistsException;
import ua.project.protester.exception.ProjectNotFoundException;
import ua.project.protester.model.Project;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.repository.project.ProjectRepository;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.project.ProjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Transactional
    @Override
    public ProjectDto createProject(ProjectDto projectDto) throws ProjectAlreadyExistsException {
        log.info("IN ProjectServiceImpl createProject, projectDto: {}", projectDto);

        Project savedProjectFromBd;

        try {
            Project projectToSave = projectMapper.toEntity(projectDto);
            projectToSave.setProjectActive(true);

            savedProjectFromBd = projectRepository.create(projectToSave);
        } catch (Exception e) {
            log.error("IN ProjectServiceImpl createProject - project {} already exists", projectDto);
            throw new ProjectAlreadyExistsException("project already exists", e);
        }

        log.info("IN ProjectServiceImpl createProject - project {} was successfully created", savedProjectFromBd);
        return projectMapper.toResponse(savedProjectFromBd);
    }

    @Transactional
    @Override
    public ProjectDto updateProject(ProjectDto projectDto) throws ProjectAlreadyExistsException {
        log.info("IN ProjectServiceImpl updateProject, projectDto: {}", projectDto);

        Project updatedProjectFromBd;

        try {
            updatedProjectFromBd = projectRepository.update(projectMapper.toEntity(projectDto));
        } catch (Exception e) {
            log.error("IN ProjectServiceImpl updateProject - project {} already exists", projectDto);
            throw new ProjectAlreadyExistsException("project already exists", e);
        }

        log.info("IN ProjectServiceImpl updateProject - project {} was successfully updated", updatedProjectFromBd);
        return projectMapper.toResponse(updatedProjectFromBd);
    }

    @Transactional
    @Override
    public ProjectDto changeProjectStatus(Long projectId) throws ProjectNotFoundException {
        log.info("IN changeProjectStatus, projectId: {}", projectId);

        Project projectToChangeStatus = getProjectById(projectId);

        Project updatedProjectFromBd = projectRepository.changeProjectStatus(
                projectToChangeStatus,
                !projectToChangeStatus.getProjectActive()
        );

        log.info("IN changeProjectStatus - project status {} was changed", updatedProjectFromBd);
        return projectMapper.toResponse(updatedProjectFromBd);
    }

    @Transactional
    @Override
    public Page<ProjectDto> findAllProjects(Pagination pagination) {
        log.info("IN findAllProjects");
        return new Page<>(
                projectRepository.findAll(pagination),
                projectRepository.getCountProjects(pagination)
        );
    }

    @Transactional
    @Override
    public Page<ProjectDto> findAllProjectsByStatus(Pagination pagination, Boolean isActive) {
        log.info("IN findAllProjectsByStatus");
        return new Page<>(
                projectRepository.findAllByStatus(pagination, isActive),
                projectRepository.getCountProjectsByStatus(pagination, isActive)
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
