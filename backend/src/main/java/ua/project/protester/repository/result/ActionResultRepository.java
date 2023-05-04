package ua.project.protester.repository.result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.executable.action.ActionNotFoundException;
import ua.project.protester.exception.executable.action.IllegalActionLogicImplementation;
import ua.project.protester.exception.result.ResultSubtypeNotFoundException;
import ua.project.protester.model.executable.AbstractAction;
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
import ua.project.protester.repository.ActionRepository;
import ua.project.protester.repository.StatusRepository;
import ua.project.protester.utils.PropertyExtractor;

import java.util.*;
import java.util.stream.Collectors;


@PropertySource("classpath:queries/action-result.properties")
@Repository
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("PMD.UnusedPrivateMethod")
public class ActionResultRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final StatusRepository statusRepository;
    private final ActionRepository actionRepository;

    @Transactional(propagation = Propagation.MANDATORY)
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

    @Transactional(propagation = Propagation.MANDATORY)
    public List<ActionResultDto> findByTestCaseResultId(Integer id) {
        return namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findAllActionResultsByTestCaseResultId"),
                new MapSqlParameterSource().addValue("id", id),
                new BeanPropertyRowMapper<>(ActionResult.class))
                .stream()
                .map(this::getBaseDto)
                .map(this::loadSubtypeData)
                .collect(Collectors.toList());
    }

    private void saveBase(Integer testCaseResultId, ActionResultDto actionResultDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveBaseActionResult"),
                new BeanPropertySqlParameterSource(getBaseModel(testCaseResultId, actionResultDto)),
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
                new BeanPropertySqlParameterSource(getRestModel(actionResultRestDto)));
        return actionResultRestDto;
    }

    private ActionResultTechnicalDto saveTechnical(Integer testCaseResultId, ActionResultTechnicalDto actionResultTechnicalDto) {
        saveBase(testCaseResultId, actionResultTechnicalDto);
        getTechnicalModel(actionResultTechnicalDto)
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
                new BeanPropertySqlParameterSource(getUiModel(actionResultUiDto)));
        return actionResultUiDto;
    }

    private ActionResultSqlDto saveSql(Integer testCaseResultId, ActionResultSqlDto actionResultSqlDto) {
        saveBase(testCaseResultId, actionResultSqlDto);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveActionResultSql"),
                new BeanPropertySqlParameterSource(getSqlModel(actionResultSqlDto)),
                keyHolder,
                new String[]{"action_result_sql_id"});
        Integer actionResultSqlId = (Integer) keyHolder.getKey();
        if (actionResultSqlDto.getColumns() != null) {
            actionResultSqlDto.getColumns()
                    .forEach(sqlColumnDto -> saveSqlColumn(actionResultSqlId, sqlColumnDto));
        }
        return actionResultSqlDto;
    }

    private void saveSqlColumn(Integer actionResultSqlId, SqlColumnDto sqlColumnDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveSqlColumn"),
                new BeanPropertySqlParameterSource(getSqlColumn(actionResultSqlId, sqlColumnDto)),
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

    public ActionResultDto loadSubtypeData(ActionResultDto resultDto) {
        if (resultDto.getAction() == null) {
            return resultDto;
        }
        try {
            switch (resultDto.getAction().getType()) {
                case REST:
                    return findRest(resultDto);
                case TECHNICAL:
                    return findTechnical(resultDto);
                case UI:
                    return findUi(resultDto);
                case SQL:
                    return findSql(resultDto);
                default:
                    log.warn("Action " + resultDto.getAction().getName() + " has incorrect type: " + resultDto.getAction().getType());
                    return resultDto;
            }
        } catch (ResultSubtypeNotFoundException e) {
            log.warn(e.getMessage(), e);
            return resultDto;
        }
    }

    private Map<String, String> findInputParametersByActionResultId(Integer id) {
        Map<String, String> parameters = new HashMap<>();
        namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findAllInputParametersByActionResultId"),
                new MapSqlParameterSource().addValue("id", id),
                (rs, rowNum) -> parameters.put(
                        rs.getString("key"),
                        rs.getString("value")));
        return parameters;
    }

    private ActionResultRestDto findRest(ActionResultDto baseDto) throws ResultSubtypeNotFoundException {
        try {
            ActionResultRest restModel = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findRestByActionResultId"),
                    new MapSqlParameterSource().addValue("id", baseDto.getId()),
                    new BeanPropertyRowMapper<>(ActionResultRest.class));

            if (restModel == null) {
                throw new ResultSubtypeNotFoundException(baseDto.getId());
            }

            return getRestDto(baseDto, restModel);
        } catch (DataAccessException e) {
            throw new ResultSubtypeNotFoundException(baseDto.getId(), e);
        }
    }

    private ActionResultTechnicalDto findTechnical(ActionResultDto baseDto) throws ResultSubtypeNotFoundException {
        try {
            List<ActionResultTechnicalExtra> technicalModel = namedParameterJdbcTemplate.query(
                    PropertyExtractor.extract(env, "findTechnicalExtraByActionResultId"),
                    new MapSqlParameterSource().addValue("id", baseDto.getId()),
                    new BeanPropertyRowMapper<>(ActionResultTechnicalExtra.class));

            return getTechnicalDto(baseDto, technicalModel);
        } catch (DataAccessException e) {
            throw new ResultSubtypeNotFoundException(baseDto.getId(), e);
        }
    }

    private ActionResultUiDto findUi(ActionResultDto baseDto) throws ResultSubtypeNotFoundException {
        try {
            ActionResultUi uiModel = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findUiByActionResultId"),
                    new MapSqlParameterSource().addValue("id", baseDto.getId()),
                    new BeanPropertyRowMapper<>(ActionResultUi.class));

            if (uiModel == null) {
                throw new ResultSubtypeNotFoundException(baseDto.getId());
            }

            return getUiDto(baseDto, uiModel);
        } catch (DataAccessException e) {
            throw new ResultSubtypeNotFoundException(baseDto.getId(), e);
        }
    }

    private ActionResultSqlDto findSql(ActionResultDto baseDto) throws ResultSubtypeNotFoundException {
        try {
            ActionResultSql sqlModel = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findSqlByActionResultId"),
                    new MapSqlParameterSource().addValue("id", baseDto.getId()),
                    new BeanPropertyRowMapper<>(ActionResultSql.class));

            if (sqlModel == null) {
                throw new ResultSubtypeNotFoundException(baseDto.getId());
            }

            return getSqlDto(baseDto, sqlModel);
        } catch (DataAccessException e) {
            throw new ResultSubtypeNotFoundException(baseDto.getId(), e);
        }
    }

    private List<SqlColumnDto> findSqlColumns(Integer actionResultSqlId) {
        return namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findSqlColumnsByActionResultSqlId"),
                new MapSqlParameterSource().addValue("id", actionResultSqlId),
                new BeanPropertyRowMapper<>(SqlColumn.class))
                .stream()
                .map(this::getSqlColumnDto)
                .collect(Collectors.toList());
    }

    private List<String> findSqlColumnCells(Integer sqlColumnId) {
        return namedParameterJdbcTemplate.queryForList(
                PropertyExtractor.extract(env, "findSqlColumnCellsValueBySqlColumnId"),
                new MapSqlParameterSource().addValue("id", sqlColumnId),
                String.class);
    }

    private ActionResult getBaseModel(Integer testCaseResultId, ActionResultDto dto) {
        return new ActionResult(
                null,
                testCaseResultId,
                dto.getAction().getId(),
                dto.getStartDate(),
                dto.getEndDate(),
                statusRepository.getIdByLabel(dto.getStatus()),
                dto.getMessage());
    }

    private ActionResultRest getRestModel(ActionResultRestDto dto) {
        return new ActionResultRest(
                dto.getId(),
                dto.getRequest(),
                dto.getResponse(),
                dto.getStatusCode());
    }

    private List<ActionResultTechnicalExtra> getTechnicalModel(ActionResultTechnicalDto dto) {
        if (dto.getExtra() == null) {
            return Collections.emptyList();
        }
        return dto.getExtra().entrySet()
                .stream()
                .map(entry -> new ActionResultTechnicalExtra(
                        dto.getId(),
                        entry.getKey(),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    private ActionResultUi getUiModel(ActionResultUiDto dto) {
        return new ActionResultUi(
                dto.getId(),
                dto.getPath());
    }

    private ActionResultSql getSqlModel(ActionResultSqlDto dto) {
        return new ActionResultSql(
                dto.getId(),
                dto.getConnectionUrl(),
                dto.getUsername(),
                dto.getQuery());
    }

    private SqlColumn getSqlColumn(Integer actionResultSqlId, SqlColumnDto dto) {
        return new SqlColumn(
                null,
                actionResultSqlId,
                dto.getName());
    }

    private ActionResultDto getBaseDto(ActionResult result) {
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
                result.getMessage());
    }

    private ActionResultRestDto getRestDto(ActionResultDto baseDto, ActionResultRest restModel) {
        return new ActionResultRestDto(
                baseDto,
                restModel.getRequest(),
                restModel.getResponse(),
                restModel.getStatusCode());
    }

    private ActionResultUiDto getUiDto(ActionResultDto baseDto, ActionResultUi uiModel) {
        return new ActionResultUiDto(
                baseDto,
                uiModel.getPath());
    }

    private ActionResultTechnicalDto getTechnicalDto(ActionResultDto baseDto, List<ActionResultTechnicalExtra> technicalModel) {
        Map<String, String> extra = new HashMap<>();
        technicalModel.forEach(entry -> extra.put(entry.getKey(), entry.getValue()));
        return new ActionResultTechnicalDto(
                baseDto,
                extra);
    }

    private ActionResultSqlDto getSqlDto(ActionResultDto baseDto, ActionResultSql sqlModel) {
        return new ActionResultSqlDto(
                baseDto,
                sqlModel.getConnectionUrl(),
                sqlModel.getUsername(),
                sqlModel.getQuery(),
                findSqlColumns(sqlModel.getId()));
    }

    private SqlColumnDto getSqlColumnDto(SqlColumn sqlColumnModel) {
        return new SqlColumnDto(
                sqlColumnModel.getName(),
                findSqlColumnCells(sqlColumnModel.getId()));
    }
}
