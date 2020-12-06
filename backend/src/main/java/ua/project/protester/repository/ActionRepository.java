package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionImplementationNotFoundException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ActionRepresentation;
import ua.project.protester.request.ActionFilter;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.PropertyExtractor;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ActionRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;

    private static void connectDbActionClass(String actionClassInDb, List<String> actionClassesInCode) {
        ListIterator<String> iterator = actionClassesInCode.listIterator();
        String className;
        while (iterator.hasNext()) {
            className = iterator.next();
            if (className.equals(actionClassInDb)) {
                iterator.remove();
                return;
            }
        }
        throw new ActionImplementationNotFoundException(
                "Failed to find " + actionClassInDb + " action implementation.");
    }

    public void syncWithDb() {
        log.info("Starting action synchronization");
        log.info("Performing action implementations scanning...");
        List<String> actionClassesInCode = scanForActionClassesInCode();
        log.info(String.format(
                "Action scan complete, found %d action implementations",
                actionClassesInCode.size()));
        log.info("Performing action classes loading from DB...");
        List<String> actionCLassesInDb = loadAllActionClassesFromDb();
        log.info(String.format(
                "Loaded %d action classes from DB. Full list: %s",
                actionCLassesInDb.size(),
                actionCLassesInDb));
        log.info("Performing integrity check...");
        List<String> newActionClasses = checkIntegrity(actionClassesInCode, actionCLassesInDb);
        log.info(String.format(
                "Integrity check complete, found %d new action implementations. Full list: %s",
                newActionClasses.size(),
                newActionClasses));
        if (newActionClasses.size() > 0) {
            log.info("Performing new actions uploading to DB...");
            uploadNewActionsToDb(newActionClasses);
            log.info("Actions uploading complete");
        }
        log.info("Action synchronization complete");
    }

    private List<String> scanForActionClassesInCode() {
        List<String> actionClassesInCode = new LinkedList<>();
        Reflections reflections = new Reflections("ua.project.protester.action");
        Set<Class<?>> actionCandidates = reflections.getTypesAnnotatedWith(Action.class);
        for (Class<?> actionCandidate : actionCandidates) {
            if (AbstractAction.class.isAssignableFrom(actionCandidate)) {
                actionClassesInCode.add(actionCandidate.getCanonicalName());
                log.info("Action implementation found: " + actionCandidate.getCanonicalName());
            } else {
                log.warn("Failed to load action class " + actionCandidate.getCanonicalName());
            }
        }
        return actionClassesInCode;
    }

    private List<String> loadAllActionClassesFromDb() {
        return namedParameterJdbcTemplate.queryForList(
                PropertyExtractor.extract(env, "findAllActionClasses"),
                new MapSqlParameterSource(),
                String.class);
    }

    private List<String> checkIntegrity(List<String> actionClassesInCode, List<String> actionClassesInDb) {
        actionClassesInDb
                .forEach(actionDeclarationInDb -> connectDbActionClass(actionDeclarationInDb, actionClassesInCode));

        return actionClassesInCode;
    }

    private void uploadNewActionsToDb(List<String> newActionClasses) {
        newActionClasses.forEach(this::uploadNewActionToDb);
    }

    private void uploadNewActionToDb(String newActionClass) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveAction"),
                new MapSqlParameterSource()
                        .addValue("className", newActionClass));
    }

    private Optional<AbstractAction> findAction(String propertyName, String parameterKey, Object parameterValue) {
        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(
                            PropertyExtractor.extract(env, propertyName),
                            new MapSqlParameterSource().addValue(parameterKey, parameterValue),
                            (rs, rowNum) -> constructAction(
                                    rs.getInt("id"),
                                    rs.getString("className"),
                                    rs.getString("description"))));
        } catch (DataAccessException e) {
            log.warn(e.toString());
            return Optional.empty();
        }
    }

    private AbstractAction constructAction(Integer id, String className, String description) {
        try {
            Class<?> actionClass = Class.forName(className);

            AbstractAction action = (AbstractAction) actionClass.getConstructor().newInstance();
            Action metadata = actionClass.getAnnotation(Action.class);

            action.init(
                    id,
                    metadata.name().isEmpty() ? actionClass.getSimpleName() : metadata.name(),
                    metadata.type(),
                    description == null || description.isEmpty() ? metadata.description() : description,
                    className,
                    metadata.parameterNames());

            return action;
        } catch (Exception e) {
            throw new ActionImplementationNotFoundException(
                    "Failed to load action implementation for class " + className);
        }
    }

    private AbstractAction constructAction(ActionRepresentation actionRepresentation) {
        return constructAction(
                actionRepresentation.getId(),
                actionRepresentation.getClassName(),
                actionRepresentation.getDescription());
    }

    public List<ActionRepresentation> findAllActionRepresentations() {
        return namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findAllActions"),
                new MapSqlParameterSource(),
                new BeanPropertyRowMapper<>(ActionRepresentation.class));
    }

    public Page<AbstractAction> findAllActions(ActionFilter actionFilter) {
        List<AbstractAction> actions = findAllActionRepresentations()
                .stream()
                .map(this::constructAction)
                .filter(abstractAction -> abstractAction.getName().startsWith(actionFilter.getFilterName()))
                .filter(abstractAction ->
                        actionFilter.getType() == null || abstractAction.getType().equals(actionFilter.getType()))
                .collect(Collectors.toList());
        if (actions.size() >= actionFilter.getOffset()) {
            return new Page<>(
                    actions.subList(
                            actionFilter.getOffset(),
                            Math.min(actions.size(), actionFilter.getOffset() + actionFilter.getPageSize())),
                    (long) actions.size());
        }
        return new Page<>(
                Collections.emptyList(),
                0L);
    }

    public Optional<AbstractAction> findActionById(Integer id) {
        return findAction(
                "findActionById",
                "id",
                id);
    }

    public Optional<AbstractAction> findActionByClassName(String className) {
        return findAction(
                "findActionByClassName",
                "className",
                className);
    }

    public Optional<AbstractAction> updateActionDescriptionById(Integer id, String newDescription) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateActionDescriptionById"),
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("description", newDescription));
        return findActionById(id);
    }
}
