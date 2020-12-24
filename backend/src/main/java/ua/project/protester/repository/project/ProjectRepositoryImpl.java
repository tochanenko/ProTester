package ua.project.protester.repository.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.exception.ProjectCreateException;
import ua.project.protester.model.Project;
import ua.project.protester.model.ProjectDto;
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.PropertyExtractor;
import ua.project.protester.utils.project.ProjectDtoRowMapper;
import ua.project.protester.utils.project.ProjectRowMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@PropertySource("classpath:queries/project.properties")
public class ProjectRepositoryImpl implements ProjectRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final ProjectRowMapper projectRowMapper;
    private final Environment env;

    @Override
    public Project create(Project project) throws ProjectCreateException {
        log.info("IN ProjectRepositoryImpl create - project: {}", project);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "createProject"),
                new MapSqlParameterSource()
                        .addValue("project_name", project.getProjectName())
                        .addValue("project_website_link", project.getProjectWebsiteLink())
                        .addValue("project_active", project.getProjectActive())
                        .addValue("creator_id", project.getCreatorId()),
                keyHolder);

        Integer id = (Integer) (Optional.ofNullable(keyHolder.getKeys())
                .orElseThrow(ProjectCreateException::new)
                .get("project_id"));

        project.setProjectId(id.longValue());

        log.info("saved project {}", project);

        return project;
    }

    @Override
    public Project update(Project project) {

        log.info("IN ProjectRepositoryImpl update - project: {}", project);

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateProject"),
                new MapSqlParameterSource()
                        .addValue("project_id", project.getProjectId())
                        .addValue("project_name", project.getProjectName())
                        .addValue("project_website_link", project.getProjectWebsiteLink())
                        .addValue("project_active", project.getProjectActive())
        );

        log.info("updated project {}", project);

        return project;
    }

    @Override
    public Project changeProjectStatus(Project project, Boolean isActive) {

        log.info("IN ProjectRepositoryImpl changeProjectStatus - project: {}, isActive: {}", project, isActive);

        namedJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateProjectStatus"),
                new MapSqlParameterSource()
                        .addValue("project_id", project.getProjectId())
                        .addValue("project_active", isActive)
        );

        project.setProjectActive(isActive);

        log.info("changed ProjectStatus - projectId: {}, isActive: {}", project, isActive);
        return project;
    }

    @Override
    public List<ProjectDto> findAll(Pagination pagination) {

        log.info("IN ProjectRepositoryImpl findAll - pagination: {}", pagination);

        return namedJdbcTemplate.query(
                PropertyExtractor.extract(env, "getAllProjects"),
                new MapSqlParameterSource()
                        .addValue("pageSize", pagination.getPageSize())
                        .addValue("offset", pagination.getOffset())
                        .addValue("filterProjectName", pagination.getSearchField() + "%"),
                new ProjectDtoRowMapper());
    }

    @Override
    public List<ProjectDto> findAllByStatus(Pagination pagination, Boolean isActive) {
        log.info("IN ProjectRepositoryImpl findAllByStatus - pagination: {}, isActive: {}", pagination, isActive);

        return namedJdbcTemplate.query(
                PropertyExtractor.extract(env, "getAllProjectsFiltered"),
                new MapSqlParameterSource()
                        .addValue("pageSize", pagination.getPageSize())
                        .addValue("offset", pagination.getOffset())
                        .addValue("filterProjectName", pagination.getSearchField() + "%")
                        .addValue("projectActive", isActive),
                new ProjectDtoRowMapper());
    }

    @Override
    public Optional<Project> findById(Long id) {
        log.info("IN ProjectRepositoryImpl findById - id: {}", id);

        try {

            return Optional.ofNullable(
                    namedJdbcTemplate.queryForObject(
                            PropertyExtractor.extract(env, "findById"),
                            new MapSqlParameterSource()
                                    .addValue("project_id", id),
                            projectRowMapper)
            );

        } catch (EmptyResultDataAccessException e) {
            log.error("IN ProjectRepositoryImpl findById - id: {} not found", id);
            return Optional.empty();
        }
    }

    @Override
    public Long getCountProjects(Pagination pagination) {
        log.info("IN ProjectRepositoryImpl getCountProjects - pagination: {}", pagination);

        return namedJdbcTemplate.queryForObject(
                PropertyExtractor.extract(env, "findCountOdRecords"),
                new MapSqlParameterSource()
                        .addValue("filterProjectName", pagination.getSearchField() + "%"),
                Long.class);
    }

    @Override
    public Long getCountProjectsByStatus(Pagination pagination, Boolean isActive) {
        log.info("IN ProjectRepositoryImpl getCountProjectsByStatus - pagination: {}, isActive: {}", pagination, isActive);

        return namedJdbcTemplate.queryForObject(
                PropertyExtractor.extract(env, "findCountOdRecordsWithStatus"),
                new MapSqlParameterSource()
                        .addValue("filterProjectName", pagination.getSearchField() + "%")
                        .addValue("projectActive", isActive),
                Long.class);
    }
}
