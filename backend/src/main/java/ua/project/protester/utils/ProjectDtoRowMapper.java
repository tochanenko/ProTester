package ua.project.protester.utils;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.project.protester.model.ProjectDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProjectDtoRowMapper implements RowMapper<ProjectDto> {

    @Override
    public ProjectDto mapRow(ResultSet resultSet, int i) throws SQLException {
        ProjectDto projectDto = new ProjectDto();

        projectDto.setProjectId(resultSet.getLong("project_id"));
        projectDto.setProjectName(resultSet.getString("project_name"));
        projectDto.setProjectWebsiteLink(resultSet.getString("project_website_link"));
        projectDto.setProjectActive(resultSet.getBoolean("project_active"));
        projectDto.setCreatorUsername(resultSet.getString("user_username"));
        projectDto.setCreatorId(resultSet.getLong("creator_id"));

        return projectDto;
    }
}

