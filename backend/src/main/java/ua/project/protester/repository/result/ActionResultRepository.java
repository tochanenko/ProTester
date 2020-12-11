package ua.project.protester.repository.result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.executable.result.ActionResult;
import ua.project.protester.repository.StatusRepository;
import ua.project.protester.utils.PropertyExtractor;

import java.util.List;
import java.util.stream.Collectors;

@PropertySource("classpath:queries/action-result.properties")
@Repository
@RequiredArgsConstructor
@Slf4j
public class ActionResultRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final StatusRepository statusRepository;
    private final ActionResultExtraRepository actionResultExtraRepository;

    public List<ActionResult> findActionResultsByTestCaseResultId(Integer id) {
        return namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findActionResultsByTestCaseResultId"),
                new MapSqlParameterSource().addValue("id", id),
                new BeanPropertyRowMapper<>(ActionResult.class))
                .stream()
                .peek(actionResult -> actionResult.setStatus(
                        statusRepository.getLabelById(actionResult.getStatusId())))
                .peek(actionResult -> actionResult.setExtra(
                        actionResultExtraRepository.findActionResultExtraByActionResultId(actionResult.getId())))
                .collect(Collectors.toList());
    }

    public ActionResult save(ActionResult actionResult) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveActionResult"),
                new MapSqlParameterSource()
                        .addValue("testCaseResultId", actionResult.getTestCaseResultId())
                        .addValue("actionName", actionResult.getActionName())
                        .addValue("startDate", actionResult.getStartDate())
                        .addValue("endDate", actionResult.getEndDate())
                        .addValue("statusId", actionResult.getStatusId() == null
                                ? statusRepository.getIdByLabel(actionResult.getStatus())
                                : actionResult.getStatusId())
                        .addValue("message", actionResult.getMessage())
                        .addValue("type", actionResult.getType().toString()),
                keyHolder,
                new String[]{"action_result_id"});
        actionResult.setId((Integer) keyHolder.getKey());
        actionResultExtraRepository.saveAll(actionResult.getId(), actionResult.getExtra());
        return actionResult;
    }
}
