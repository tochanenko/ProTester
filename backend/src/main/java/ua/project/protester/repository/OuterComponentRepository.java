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
import ua.project.protester.exception.executable.ExecutableComponentNotFoundException;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.ExecutableComponent;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.Step;
import ua.project.protester.utils.PropertyExtractor;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

@PropertySource("classpath:queries/outer-component.properties")
@Repository
@RequiredArgsConstructor
@Slf4j
public class OuterComponentRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final ActionRepository actionRepository;
    private final StepParameterRepository stepParameterRepository;

    public void saveOuterComponent(OuterComponent outerComponent, boolean isCompound) {
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

        ListIterator<Step> outerComponentStepsIterator = outerComponent.getSteps().listIterator();
        Step outerComponentStep;
        while (outerComponentStepsIterator.hasNext()) {
            int outerComponentStepOrder = outerComponentStepsIterator.nextIndex();
            outerComponentStep = outerComponentStepsIterator.next();
            saveOuterComponentStep(outerComponentId, isCompound, outerComponentStep, outerComponentStepOrder);
        }
    }

    public List<OuterComponent> findAllOuterComponents(boolean areCompounds) {
        String sql = areCompounds
                ? PropertyExtractor.extract(env, "findAllCompounds")
                : PropertyExtractor.extract(env, "findAllTestScenarios");

        List<OuterComponent> allOuterComponents = namedParameterJdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(OuterComponent.class));

        ExecutableComponentType componentsType = areCompounds
                ? ExecutableComponentType.COMPOUND
                : ExecutableComponentType.TEST_SCENARIO;

        allOuterComponents
                .forEach(outerComponent -> {
                    outerComponent.setType(componentsType);
                    outerComponent.setSteps(
                            findAllOuterComponentStepsById(outerComponent.getId(), areCompounds));
                });

        return allOuterComponents;
    }

    public Optional<OuterComponent> findOuterComponentById(Integer id, boolean isCompound) throws OuterComponentNotFoundException {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "findCompoundById")
                : PropertyExtractor.extract(env, "findTestScenarioById");

        try {
            OuterComponent outerComponent = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    new MapSqlParameterSource().addValue("id", id),
                    new BeanPropertyRowMapper<>(OuterComponent.class));
            if (outerComponent == null) {
                return Optional.empty();
            }

            List<Step> steps = findAllOuterComponentStepsById(outerComponent.getId(), isCompound);
            outerComponent.setType(isCompound ? ExecutableComponentType.COMPOUND : ExecutableComponentType.TEST_SCENARIO);
            outerComponent.setSteps(steps);
            return Optional.of(outerComponent);
        } catch (DataAccessException e) {
            throw new OuterComponentNotFoundException();
        }
    }

    public void deleteOuterComponentById(Integer id, boolean isCompound) {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "deleteCompoundById")
                : PropertyExtractor.extract(env, "deleteTestScenarioById");

        namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource().addValue("id", id));
    }

    public void deleteOuterComponentByName(String name, boolean isCompound) {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "deleteCompoundByName")
                : PropertyExtractor.extract(env, "deleteTestScenarioByName");

        namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource().addValue("name", name));
    }

    public boolean existsOuterComponentWithId(int id, boolean isCompound) {
        String sql = isCompound
                ? PropertyExtractor.extract(env, "findOuterCompoundIdByOuterCompoundId")
                : PropertyExtractor.extract(env, "findOuterTestScenarioIdByOuterTestScenarioId");

        try {
            return null != namedParameterJdbcTemplate.queryForObject(
                    sql,
                    new MapSqlParameterSource().addValue("id", id),
                    Integer.class);
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean compoundWithIdIsInnerComponent(int id) {
        try {
            return null != namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findInnerCompoundIdByInnerCompoundId"),
                    new MapSqlParameterSource().addValue("id", id),
                    Integer.class);
        } catch (DataAccessException e) {
            return false;
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

    private Step initOuterComponentStep(int id, boolean isAction, int actionId, int outerComponentId) {
        try {
            ExecutableComponent component =
                    isAction
                            ?
                            actionRepository.findActionById(actionId).orElseThrow(ExecutableComponentNotFoundException::new)
                            :
                            findOuterComponentById(outerComponentId, true).orElseThrow(ExecutableComponentNotFoundException::new);

            Map<String, String> parameters = stepParameterRepository.findAllDataSetParamsId(id);

            return new Step(id, isAction, component, parameters);
        } catch (OuterComponentNotFoundException e) {
            throw new ExecutableComponentNotFoundException();
        }
    }
}
