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
import ua.project.protester.exception.CompoundNotFoundException;
import ua.project.protester.exception.ExecutableComponentNotFoundException;
import ua.project.protester.model.executable.Step;
import ua.project.protester.model.executable.Compound;
import ua.project.protester.model.executable.ExecutableComponent;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.utils.PropertyExtractor;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

@PropertySource("classpath:queries/compound.properties")
@Repository
@RequiredArgsConstructor
@Slf4j
public class CompoundRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final ActionRepository actionRepository;
    private final StepParameterRepository stepParameterRepository;

    public void saveCompound(Compound compound) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveCompound"),
                new BeanPropertySqlParameterSource(compound),
                keyHolder,
                new String[]{"compound_id"});
        Integer compoundId = (Integer) keyHolder.getKey();

        ListIterator<Step> compoundStepsIterator = compound.getSteps().listIterator();
        Step compoundStep;
        while (compoundStepsIterator.hasNext()) {
            int compoundStepOrder = compoundStepsIterator.nextIndex();
            compoundStep = compoundStepsIterator.next();
            saveCompoundStep(compoundId, compoundStep, compoundStepOrder);
        }
    }

    public List<Compound> findAll() {
        List<Compound> allCompounds = namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findAllCompounds"),
                new BeanPropertyRowMapper<>(Compound.class));

        allCompounds
                .forEach(compound -> {
                    compound.setType(ExecutableComponentType.COMPOUND);
                    compound.setSteps(
                            findAllCompoundStepsById(compound.getId()));
                });

        return allCompounds;
    }

    public Optional<Compound> findCompoundById(Integer id) throws CompoundNotFoundException {
        try {
            Compound compound = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findCompoundById"),
                    new MapSqlParameterSource().addValue("id", id),
                    new BeanPropertyRowMapper<>(Compound.class));
            if (compound == null) {
                return Optional.empty();
            }
            List<Step> steps = findAllCompoundStepsById(compound.getId());
            compound.setType(ExecutableComponentType.COMPOUND);
            compound.setSteps(steps);
            return Optional.of(compound);
        } catch (DataAccessException e) {
            throw new CompoundNotFoundException();
        }
    }

    public void deleteCompoundById(Integer id) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "deleteCompoundById"),
                new MapSqlParameterSource().addValue("id", id));
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

    private void saveCompoundStep(Integer outerCompoundId, Step step, int stepOrder) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveCompoundStep"),
                new MapSqlParameterSource()
                        .addValue("outerIsCompound", true)
                        .addValue("outerCompoundId", outerCompoundId)
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

    private List<Step> findAllCompoundStepsById(Integer id) {
        return namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findAllCompoundStepsById"),
                new MapSqlParameterSource()
                        .addValue("id", id),
                (rs, rowNum) -> initCompoundStep(
                        rs.getInt("id"),
                        rs.getBoolean("isAction"),
                        rs.getInt("actionId"),
                        rs.getInt("compoundId")));
    }

    private Step initCompoundStep(int id, boolean isAction, int actionId, int compoundId) {
        try {
            ExecutableComponent component =
                    isAction
                            ?
                            actionRepository.findActionById(actionId).orElseThrow(ExecutableComponentNotFoundException::new)
                            :
                            findCompoundById(compoundId).orElseThrow(ExecutableComponentNotFoundException::new);

            Map<String, String> parameters = stepParameterRepository.findAllByStepId(id);

            return new Step(id, isAction, component, parameters);
        } catch (CompoundNotFoundException e) {
            throw new ExecutableComponentNotFoundException();
        }
    }
}
