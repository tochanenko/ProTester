package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.Project;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.ProjectDtoRowMapper;
import ua.project.protester.utils.ProjectRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:queries/project.properties")
public class ProjectRepositoryImpl implements ProjectRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final ProjectRowMapper projectRowMapper;

    @Value("${CREATE_PROJECT}")
    private String createProject;

    @Value("${UPDATE_PROJECT}")
    private String updateProject;

    @Value("${UPDATE_PROJECT_STATUS}")
    private String updateProjectStatus;

    @Value("${GET_ALL_PROJECTS}")
    private String getAllProjects;

    @Value("${GET_ALL_PROJECTS_FILTERED}")
    private String getAllProjectsFiltered;

    @Value("${FIND_BY_ID}")
    private String findById;

    @Value("${FIND_COUNT_OF_RECORDS}")
    private String findCountOdRecords;

    @Override
    public void create(Project project) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("project_name", project.getProjectName());
        namedParams.addValue("project_website_link", project.getProjectWebsiteLink());
        namedParams.addValue("project_active", project.getProjectActive());
        namedParams.addValue("creator_id", project.getCreatorId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(createProject, namedParams, keyHolder);

        Integer id = (Integer) (keyHolder.getKeys().get("project_id"));
        project.setProjectId(id.longValue());
    }

    @Override
    public void update(Project project) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("project_id", project.getProjectId());
        namedParams.addValue("project_name", project.getProjectName());
        namedParams.addValue("project_website_link", project.getProjectWebsiteLink());
        namedParams.addValue("project_active", project.getProjectActive());

        namedJdbcTemplate.update(updateProject, namedParams);
    }

    @Override
    public void changeProjectStatus(Long id, Boolean isActive) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("project_id", id);
        namedParams.addValue("project_active", isActive);

        namedJdbcTemplate.update(updateProjectStatus, namedParams);
    }

    @Override
    public List<ProjectDto> findAll(Pagination pagination) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("pageSize", pagination.getPageSize());
        namedParams.addValue("offset", pagination.getOffset());
        namedParams.addValue("filterProjectName", pagination.getProjectName() + "%");

        return namedJdbcTemplate.query(getAllProjects, namedParams, new ProjectDtoRowMapper());
    }

    @Override
    public List<ProjectDto> findAllFilteredByStatus(Pagination pagination) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("pageSize", pagination.getPageSize());
        namedParams.addValue("offset", pagination.getOffset());
        namedParams.addValue("filterProjectName", pagination.getProjectName() + "%");
        namedParams.addValue("projectActive", pagination.getProjectActive());

        return namedJdbcTemplate.query(getAllProjectsFiltered, namedParams, new ProjectDtoRowMapper());
    }

    @Override
    public Optional<Project> findById(Long id) {
        try {
            MapSqlParameterSource namedParams = new MapSqlParameterSource();
            namedParams.addValue("project_id", id);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(findById, namedParams, projectRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Long getCountOfAllProjects() {
        return namedJdbcTemplate.queryForObject(findCountOdRecords, new MapSqlParameterSource(), Long.class);
    }

}
