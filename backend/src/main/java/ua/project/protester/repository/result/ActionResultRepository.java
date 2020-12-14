package ua.project.protester.repository.result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.exception.executable.action.IllegalActionLogicImplementation;
import ua.project.protester.model.executable.result.ActionResult;
import ua.project.protester.model.executable.result.ActionResultDto;
import ua.project.protester.model.executable.result.subtype.ActionResultRest;
import ua.project.protester.model.executable.result.subtype.ActionResultRestDto;
import ua.project.protester.model.executable.result.subtype.ActionResultSql;
import ua.project.protester.model.executable.result.subtype.ActionResultSqlDto;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalExtra;
import ua.project.protester.model.executable.result.subtype.ActionResultUi;
import ua.project.protester.model.executable.result.subtype.ActionResultUiDto;
import ua.project.protester.model.executable.result.subtype.SqlColumn;
import ua.project.protester.model.executable.result.subtype.SqlColumnDto;
import ua.project.protester.repository.StatusRepository;
import ua.project.protester.utils.PropertyExtractor;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;


// TODO: write sql queries
@PropertySource("classpath:queries/action-result.properties")
@Repository
@RequiredArgsConstructor
@Slf4j
public class ActionResultRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final StatusRepository statusRepository;
    // private final ActionRepository actionRepository;

    public ActionResultDto save(Integer testCaseResultId, ActionResultDto actionResult) throws IllegalActionLogicImplementation {
        switch (actionResult.getAction().getType()) {
            case REST:
                if (actionResult instanceof ActionResultRestDto) {
                    return saveRest(testCaseResultId, (ActionResultRestDto) actionResult);
                }
                break;
            case TECHNICAL:
                if (actionResult instanceof ActionResultTechnicalDto) {
                    return saveTechnical(testCaseResultId, (ActionResultTechnicalDto) actionResult);
                }
                break;
            case UI:
                if (actionResult instanceof ActionResultUiDto) {
                    return saveUi(testCaseResultId, (ActionResultUiDto) actionResult);
                }
                break;
            case SQL:
                if (actionResult instanceof ActionResultSqlDto) {
                    return saveSql(testCaseResultId, (ActionResultSqlDto) actionResult);
                }
                break;
            default:
                throw new IllegalActionLogicImplementation(
                        "Action '" + actionResult.getAction().getName()
                                + "' has illegal type: " + actionResult.getAction().getType());
        }
        throw new IllegalActionLogicImplementation(
                "Action '" + actionResult.getAction().getName() + "' has type " + actionResult.getAction().getType()
                        + ", but actual result type is not instance of appropriate result subtype");
    }

    public List<ActionResultDto> findByTestCaseResultId(Integer id) {
        // TODO: implement
        log.warn(id.toString());
        return null;
    }

    private void saveBase(Integer testCaseResultId, ActionResultDto actionResultDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveBaseActionResult"),
                new BeanPropertySqlParameterSource(getBaseModelFromDto(testCaseResultId, actionResultDto)),
                keyHolder,
                new String[]{"action_result_id"});
        actionResultDto.setId((Integer) keyHolder.getKey());
        saveInputParameters(actionResultDto);
    }

    private void saveInputParameters(ActionResultDto actionResultDto) {
        actionResultDto.getInputParameters()
                .forEach((key, value) ->
                        namedParameterJdbcTemplate.update(
                                PropertyExtractor.extract(env, "saveInputParameter"),
                                new MapSqlParameterSource()
                                        .addValue("actionResultId", actionResultDto.getId())
                                        .addValue("key", key)
                                        .addValue("value", value)));
    }

    private ActionResultRestDto saveRest(Integer testCaseResultId, ActionResultRestDto actionResultRestDto) {
        saveBase(testCaseResultId, actionResultRestDto);
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveActionResultRest"),
                new BeanPropertySqlParameterSource(getRestModelFromDto(actionResultRestDto)));
        return actionResultRestDto;
    }

    private ActionResultTechnicalDto saveTechnical(Integer testCaseResultId, ActionResultTechnicalDto actionResultTechnicalDto) {
        saveBase(testCaseResultId, actionResultTechnicalDto);
        getTechnicalModelFromDto(actionResultTechnicalDto)
                .forEach(actionResultTechnicalExtra ->
                        namedParameterJdbcTemplate.update(
                                PropertyExtractor.extract(env, "saveActionResultTechnicalExtra"),
                                new BeanPropertySqlParameterSource(actionResultTechnicalExtra)));
        return actionResultTechnicalDto;
    }

    private ActionResultUiDto saveUi(Integer testCaseResultId, ActionResultUiDto actionResultUiDto) {
        saveBase(testCaseResultId, actionResultUiDto);
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveActionResultUi"),
                new BeanPropertySqlParameterSource(getUiModelFromDto(actionResultUiDto)));
        return actionResultUiDto;
    }

    private ActionResultSqlDto saveSql(Integer testCaseResultId, ActionResultSqlDto actionResultSqlDto) {
        saveBase(testCaseResultId, actionResultSqlDto);
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveActionResultSql"),
                new BeanPropertySqlParameterSource(getSqlModelFromDto(actionResultSqlDto)));
        actionResultSqlDto.getColumns()
                .forEach(sqlColumnDto -> saveSqlColumn(actionResultSqlDto.getId(), sqlColumnDto));
        return actionResultSqlDto;
    }

    private void saveSqlColumn(Integer actionResultSqlId, SqlColumnDto sqlColumnDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveSqlColumn"),
                new BeanPropertySqlParameterSource(getSqlColumnFromDto(actionResultSqlId, sqlColumnDto)),
                keyHolder,
                new String[]{"sql_column_id"});
        sqlColumnDto.setId((Integer) keyHolder.getKey());
        saveSqlColumnCells(sqlColumnDto);
    }

    private void saveSqlColumnCells(SqlColumnDto sqlColumnDto) {
        ListIterator<String> iterator = sqlColumnDto.getRows().listIterator();
        int order = 0;
        while (iterator.hasNext()) {
            String value = iterator.next();
            namedParameterJdbcTemplate.update(
                    PropertyExtractor.extract(env, "saveSqlColumnCell"),
                    new MapSqlParameterSource()
                            .addValue("sqlColumnId", sqlColumnDto.getId())
                            .addValue("orderNumber", order++)
                            .addValue("value", value));
        }
    }

    /*
    private Map<String, String> findInputParametersByActionResultId(Integer id) {
        // TODO: implement
        log.warn(id.toString());
        return null;
    }

    private ActionResultRestDto findRestById(Integer id) {
        // TODO: implement
        log.warn(id.toString());
        return null;
    }

    private ActionResultTechnicalDto findTechnicalById(Integer id) {
        // TODO: implement
        log.warn(id.toString());
        return null;
    }

    private ActionResultUiDto findUiById(Integer id) {
        // TODO: implement
        log.warn(id.toString());
        return null;
    }

    private ActionResultSqlDto findSqlById(Integer id) {
        // TODO: implement
        log.warn(id.toString());
        return null;
    }
    */

    private ActionResult getBaseModelFromDto(Integer testCaseResultId, ActionResultDto dto) {
        return new ActionResult(
                null,
                testCaseResultId,
                dto.getAction().getId(),
                dto.getStartDate(),
                dto.getEndDate(),
                statusRepository.getIdByLabel(dto.getStatus()),
                dto.getException().getMessage());
    }

    private ActionResultRest getRestModelFromDto(ActionResultRestDto dto) {
        return new ActionResultRest(
                dto.getId(),
                dto.getRequest(),
                dto.getResponse(),
                dto.getStatusCode());
    }

    private List<ActionResultTechnicalExtra> getTechnicalModelFromDto(ActionResultTechnicalDto dto) {
        return dto.getExtra().entrySet()
                .stream()
                .map(entry -> new ActionResultTechnicalExtra(
                        dto.getId(),
                        entry.getKey(),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    private ActionResultUi getUiModelFromDto(ActionResultUiDto dto) {
        return new ActionResultUi(
                dto.getId(),
                dto.getPath());
    }

    private ActionResultSql getSqlModelFromDto(ActionResultSqlDto dto) {
        return new ActionResultSql(
                dto.getId(),
                dto.getConnectionUrl(),
                dto.getUsername(),
                dto.getQuery());
    }

    private SqlColumn getSqlColumnFromDto(Integer actionResultSqlId, SqlColumnDto dto) {
        return new SqlColumn(
                null,
                actionResultSqlId,
                dto.getName());
    }

    // TODO: uncomment
    /*
    private ActionResultDto getBaseDtoFromModel(ActionResult result) {
        AbstractAction action;
        try {
            action = actionRepository.findActionById(result.getActionId());
        } catch (ActionNotFoundException e) {
            log.warn(e.getMessage());
            action = null;
        }
        return new ActionResultDto(
                result.getId(),
                action,
                result.getStartDate(),
                result.getEndDate(),
                statusRepository.getLabelById(result.getStatusId()),
                findInputParametersByActionResultId(result.getId()),
                new ActionExecutionException(result.getMessage()));
    }
    */
}
