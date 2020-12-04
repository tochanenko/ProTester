package ua.project.protester.utils;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.project.protester.model.Project;
import ua.project.protester.model.ProjectDto;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final ModelMapper modelMapper;

    public Project toEntity(ProjectDto projectDto) {
        return modelMapper.map(projectDto, Project.class);
    }

    public ProjectDto toResponse(Project project) {
        return modelMapper.map(project, ProjectDto.class);
    }

}
