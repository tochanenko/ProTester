package ua.project.protester.utils.project;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.project.protester.model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProjectRowMapper implements RowMapper<Project> {
    
    @Override
    public Project mapRow(ResultSet resultSet, int i) throws SQLException {
        Project project = new Project();

        project.setProjectId(resultSet.getLong("project_id"));
        project.setProjectName(resultSet.getString("project_name"));
        project.setProjectWebsiteLink(resultSet.getString("project_website_link"));
        project.setProjectActive(resultSet.getBoolean("project_active"));
        project.setCreatorId(resultSet.getLong("creator_id"));

        return project;
    }
}
