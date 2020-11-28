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
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.ProjectMapper;

import java.util.List;
import java.util.Optional;


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

        checkUniqueProject(projectDto);

        Project projectToSave = projectMapper.toEntity(projectDto);
        projectToSave.setProjectActive(true);

        projectRepository.create(projectToSave);

        log.info("IN createProject - project {} was successfully created", projectToSave);

        return projectMapper.toResponse(projectToSave);
    }

    @Transactional
    @Override
    public ProjectDto updateProject(ProjectDto projectDto) throws ProjectAlreadyExistsException {
        log.info("IN updateProject");

        checkUniqueProject(projectDto);

        Project projectToUpdate = projectMapper.toEntity(projectDto);
        projectRepository.update(projectToUpdate);

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
    public List<ProjectDto> findAllProjects(Pagination pagination) {
        log.info("IN findAllProjects");
        return pagination.isFilterPresent()
                ? projectRepository.findAllFilteredByStatus(pagination)
                : projectRepository.findAll(pagination);
    }

    @Override
    public Long getCountOfAllProjects() {
        log.info("IN getCountOfAllProjects");
        return projectRepository.getCountOfAllProjects();
    }

    @Override
    public ProjectDto getProjectDtoById(Long id) throws ProjectNotFoundException {
        log.info("IN getProjectDtoById");

        Project project = getProjectById(id);

        log.info("IN getProjectDtoById - project with id {} was found", id);
        return projectMapper.toResponse(project);
    }


    private void checkUniqueProject(ProjectDto projectDto) throws ProjectAlreadyExistsException {
        Optional<Project> projectByName = projectRepository.findProjectByName(projectDto.getProjectName());
        Optional<Project> projectByLink = projectRepository.findProjectByWebsiteLink(projectDto.getProjectWebsiteLink());

        if (projectByLink.isPresent() || projectByName.isPresent()) {
            log.error("IN checkUniqueProject - project {} already exists", projectDto);
            throw new ProjectAlreadyExistsException(
                    String.format("project wit name=%s or link=%s already exists", projectDto.getProjectName(),
                            projectDto.getProjectWebsiteLink())
            );
        }
    }

    private Project getProjectById(Long id) throws ProjectNotFoundException {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Can not find project with id=" + id));
    }
}
