package ua.project.protester.repository;

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
import ua.project.protester.exception.executable.ExecutableComponentNotFoundException;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.exception.executable.OuterComponentStepSaveException;
import ua.project.protester.exception.executable.action.ActionNotFoundException;
import ua.project.protester.model.executable.ExecutableComponent;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.Step;
import ua.project.protester.request.OuterComponentFilter;
import ua.project.protester.response.LightOuterComponentResponse;
import ua.project.protester.utils.PropertyExtractor;

import java.util.*;

@PropertySource("classpath:queries/outer-component.properties")
@Repository
@RequiredArgsConstructor
@Slf4j
public class OuterComponentRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final ActionRepository actionRepository;
    private final StepParameterRepository stepParameterRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public Optional<OuterComponent> saveOuterComponent(OuterComponent outerComponent, boolean isCompound) throws OuterComponentStepSaveException {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "saveCompound")
                : PropertyExtractor.extract(env, "saveTestScenario");
        String idColumnName = isCompound ? "compound_id" : "scenario_id";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                sql,
                new BeanPropertySqlParameterSource(outerComponent),
                keyHolder,
                new String[]{idColumnName});
        Integer outerComponentId = (Integer) keyHolder.getKey();

        saveOuterComponentSteps(outerComponent, outerComponentId, isCompound);

        try {
            return Optional.of(findOuterComponentById(outerComponentId, isCompound));
        } catch (OuterComponentNotFoundException e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<OuterComponent> findAllOuterComponents(boolean areCompounds, OuterComponentFilter filter, boolean loadSteps) {
        String sql = areCompounds
                ? PropertyExtractor.extract(env, "findAllCompounds")
                : PropertyExtractor.extract(env, "findAllTestScenarios");

        List<OuterComponent> allOuterComponents = namedParameterJdbcTemplate.query(
                sql,
                new MapSqlParameterSource()
                        .addValue("pageSize", filter.getPageSize())
                        .addValue("offset", filter.getOffset())
                        .addValue("filterName", filter.getOuterComponentName() + '%'),
                new BeanPropertyRowMapper<>(OuterComponent.class));

        ExecutableComponentType componentsType = areCompounds
                ? ExecutableComponentType.COMPOUND
                : ExecutableComponentType.TEST_SCENARIO;

        allOuterComponents
                .forEach(outerComponent -> outerComponent.setType(componentsType));
        if (loadSteps) {
            allOuterComponents
                    .forEach(outerComponent ->
                            outerComponent.setSteps(
                                    findAllOuterComponentStepsById(outerComponent.getId(), areCompounds)));
        } else {
            allOuterComponents
                    .forEach(outerComponent ->
                            outerComponent.setSteps(Collections.emptyList()));
        }

        return allOuterComponents;
    }

    public Long countOuterComponents(boolean isCompound, OuterComponentFilter filter) {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "countCompounds")
                : PropertyExtractor.extract(env, "countTestScenarios");

        return namedParameterJdbcTemplate.queryForObject(
                sql,
                new MapSqlParameterSource()
                        .addValue("filterName", filter.getOuterComponentName() + '%'),
                Long.class);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public OuterComponent findOuterComponentById(Integer id, boolean isCompound) throws OuterComponentNotFoundException {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "findCompoundById")
                : PropertyExtractor.extract(env, "findTestScenarioById");

        try {
            OuterComponent outerComponent = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    new MapSqlParameterSource().addValue("id", id),
                    new BeanPropertyRowMapper<>(OuterComponent.class));
            if (outerComponent == null) {
                throw new OuterComponentNotFoundException(id, isCompound);
            }

            List<Step> steps = findAllOuterComponentStepsById(outerComponent.getId(), isCompound);
            outerComponent.setType(isCompound ? ExecutableComponentType.COMPOUND : ExecutableComponentType.TEST_SCENARIO);
            outerComponent.setSteps(steps);
            return outerComponent;
        } catch (DataAccessException e) {
            throw new OuterComponentNotFoundException(id, isCompound, e);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Optional<OuterComponent> deleteOuterComponentById(Integer id, boolean isCompound) {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "deleteCompoundById")
                : PropertyExtractor.extract(env, "deleteTestScenarioById");

        Optional<OuterComponent> deletedOuterComponent;
        try {
            deletedOuterComponent = Optional.of(findOuterComponentById(id, isCompound));
        } catch (OuterComponentNotFoundException e) {
            log.warn(e.getMessage());
            deletedOuterComponent = Optional.empty();
        }
        namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource().addValue("id", id));
        return deletedOuterComponent;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Optional<OuterComponent> updateOuterComponent(int id, OuterComponent updatedOuterComponent, boolean isCompound) throws OuterComponentStepSaveException {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "updateCompound")
                : PropertyExtractor.extract(env, "updateTestScenario");
        int updatedRows = namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("name", updatedOuterComponent.getName())
                        .addValue("description", updatedOuterComponent.getDescription()));

        if (updatedRows == 0) {
            return Optional.empty();
        }

        deleteOuterComponentSteps(id, isCompound);
        saveOuterComponentSteps(updatedOuterComponent, id, isCompound);
        try {
            return Optional.of(findOuterComponentById(id, isCompound));
        } catch (OuterComponentNotFoundException e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

    public List<LightOuterComponentResponse> findOuterComponentsByInnerCompoundId(int id) {
        try {
            return namedParameterJdbcTemplate.query(
                    PropertyExtractor.extract(env, "findOuterComponentsByInnerCompoundId"),
                    new MapSqlParameterSource().addValue("id", id),
                    new BeanPropertyRowMapper<>(LightOuterComponentResponse.class));
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    private void deleteOuterComponentSteps(Integer id, boolean isCompound) {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "deleteStepsByCompoundId")
                : PropertyExtractor.extract(env, "deleteStepsByTestScenarioId");
        namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource().addValue("id", id));
    }

    private void saveOuterComponentSteps(OuterComponent outerComponent, Integer outerComponentId, boolean isCompound) throws OuterComponentStepSaveException {
        ListIterator<Step> outerComponentStepsIterator = outerComponent.getSteps().listIterator();
        Step outerComponentStep;
        while (outerComponentStepsIterator.hasNext()) {
            int outerComponentStepOrder = outerComponentStepsIterator.nextIndex();
            outerComponentStep = outerComponentStepsIterator.next();
            try {
                saveOuterComponentStep(outerComponentId, isCompound, outerComponentStep, outerComponentStepOrder);
            } catch (DataAccessException e) {
                throw new OuterComponentStepSaveException(e);
            }
        }
    }

    private void saveOuterComponentStep(Integer outerComponentId, boolean outerComponentIsCompound, Step step, int stepOrder) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveStep"),
                new MapSqlParameterSource()
                        .addValue("outerIsCompound", outerComponentIsCompound)
                        .addValue("outerCompoundId", outerComponentIsCompound ? outerComponentId : null)
                        .addValue("outerTestScenarioId", outerComponentIsCompound ? null : outerComponentId)
                        .addValue("innerIsAction", step.isAction())
                        .addValue("innerActionId", step.isAction() ? step.getId() : null)
                        .addValue("innerCompoundId", step.isAction() ? null : step.getId())
                        .addValue("stepOrder", stepOrder),
                keyHolder,
                new String[]{"step_id"});
        Map<String, String> parameters = step.getParameters();
        stepParameterRepository.saveAll(
                (Integer) keyHolder.getKey(),
                parameters);
    }

    private List<Step> findAllOuterComponentStepsById(Integer id, boolean isCompound) {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "findAllCompoundStepsById")
                : PropertyExtractor.extract(env, "findAllTestScenarioStepsById");

        return namedParameterJdbcTemplate.query(
                sql,
                new MapSqlParameterSource()
                        .addValue("id", id),
                (rs, rowNum) -> initOuterComponentStep(
                        rs.getInt("id"),
                        rs.getBoolean("isAction"),
                        rs.getInt("actionId"),
                        rs.getInt("compoundId")));
    }

    public List<Step> findAllOuterComponentStepsByIdResult(Integer id, boolean isCompound) {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "findAllCompoundStepsByIdResult")
                : PropertyExtractor.extract(env, "findAllTestScenarioStepsByIdResult");

        return namedParameterJdbcTemplate.query(
                sql,
                new MapSqlParameterSource()
                        .addValue("id", id),
                (rs, rowNum) -> initOuterComponentStep(
                        rs.getInt("id"),
                        rs.getBoolean("isAction"),
                        rs.getInt("actionId"),
                        rs.getInt("compoundId")));
    }

    public int findCompoundId(int testScenarioId) {
        String sql = PropertyExtractor.extract(env, "findCompoundIdWhenItIsNotTestScenario");
        return namedParameterJdbcTemplate.queryForObject(
                sql,
                new MapSqlParameterSource()
                        .addValue("id", testScenarioId), Integer.class);
    }

    private Step initOuterComponentStep(int id, boolean isAction, int actionId, int outerComponentId) {
        try {
            ExecutableComponent component =
                    isAction
                            ? actionRepository.findActionById(actionId)
                            : findOuterComponentById(outerComponentId, true);

            Map<String, String> parameters = stepParameterRepository.findAllDataSetParamsId(id);

            return new Step(id, isAction, component, parameters);
        } catch (OuterComponentNotFoundException | ActionNotFoundException e) {
            throw new ExecutableComponentNotFoundException(e);
        }
    }
}
