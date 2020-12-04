package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionImplementationNotFoundException;
import ua.project.protester.model.executable.AbstractAction;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ActionRepository {

    private static final String PROPERTY_NOT_FOUND_TEMPLATE = "Could not find property '%s' in queries/action.properties";
    private final Logger logger = LoggerFactory.getLogger(ActionRepository.class);
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
        logger.info("Starting action synchronization");
        logger.info("Performing action implementations scanning...");
        List<String> actionClassesInCode = scanForActionClassesInCode();
        logger.info(String.format(
                "Action scan complete, found %d action implementations",
                actionClassesInCode.size()));
        logger.info("Performing action classes loading from DB...");
        List<String> actionCLassesInDb = loadAllActionClassesFromDb();
        logger.info(String.format(
                "Loaded %d action classes from DB. Full list: %s",
                actionCLassesInDb.size(),
                actionCLassesInDb));
        logger.info("Performing integrity check...");
        List<String> newActionClasses = checkIntegrity(actionClassesInCode, actionCLassesInDb);
        logger.info(String.format(
                "Integrity check complete, found %d new action implementations. Full list: %s",
                newActionClasses.size(),
                newActionClasses));
        if (newActionClasses.size() > 0) {
            logger.info("Performing new actions uploading to DB...");
            uploadNewActionsToDb(newActionClasses);
            logger.info("Actions uploading complete");
        }
        logger.info("Action synchronization complete");
    }

    private List<String> scanForActionClassesInCode() {
        List<String> actionClassesInCode = new LinkedList<>();
        Reflections reflections = new Reflections("ua.project.protester.action");
        Set<Class<?>> actionCandidates = reflections.getTypesAnnotatedWith(Action.class);
        for (Class<?> actionCandidate : actionCandidates) {
            if (AbstractAction.class.isAssignableFrom(actionCandidate)) {
                actionClassesInCode.add(actionCandidate.getCanonicalName());
                logger.info("Action implementation found: " + actionCandidate.getCanonicalName());
            } else {
                logger.warn("Failed to load action class " + actionCandidate.getCanonicalName());
            }
        }
        return actionClassesInCode;
    }

    private List<String> loadAllActionClassesFromDb() {
        String propertyName = "findAllActionClasses";
        try {
            return namedParameterJdbcTemplate.queryForList(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new MapSqlParameterSource(),
                    String.class);
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
            return Collections.emptyList();
        }
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
        String propertyName = "saveAction";
        try {
            namedParameterJdbcTemplate.update(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new MapSqlParameterSource()
                            .addValue("className", newActionClass));
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
        }
    }

    private Optional<AbstractAction> findAction(String propertyName, String parameterKey, Object parameterValue) {
        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(
                            Objects.requireNonNull(env.getProperty(propertyName)),
                            new MapSqlParameterSource().addValue(parameterKey, parameterValue),
                            (rs, rowNum) -> constructAction(
                                    rs.getInt("id"),
                                    rs.getString("className"))));
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
            return Optional.empty();
        } catch (DataAccessException e) {
            logger.warn(e.toString());
            return Optional.empty();
        }
    }

    private AbstractAction constructAction(Integer id, String className) {
        try {
            Class<?> actionClass = Class.forName(className);

            AbstractAction action = (AbstractAction) actionClass.getConstructor().newInstance();
            Action metadata = actionClass.getAnnotation(Action.class);

            action.init(
                    id,
                    metadata.name().isEmpty() ? actionClass.getSimpleName() : metadata.name(),
                    metadata.type(),
                    metadata.description(),
                    className,
                    metadata.parameterNames());

            return action;
        } catch (Exception e) {
            throw new ActionImplementationNotFoundException(
                    "Failed to load action implementation for class " + className);
        }
    }

    private AbstractAction constructAction(Map.Entry<Integer, String> entry) {
        return constructAction(entry.getKey(), entry.getValue());
    }

    public Map<Integer, String> findAllActionRepresentations() {
        String propertyName = "findAllActions";
        try {
            Map<Integer, String> actionRepresentations = new HashMap<>();
            namedParameterJdbcTemplate.query(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new MapSqlParameterSource(),
                    (rs, rowNum) -> actionRepresentations.put(
                            rs.getInt("id"),
                            rs.getString("className")));
            return actionRepresentations;
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
            return Collections.emptyMap();
        }
    }

    public List<AbstractAction> findAllActions() {
        return findAllActionRepresentations().entrySet()
                .stream()
                .map(this::constructAction)
                .collect(Collectors.toList());
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

    public Optional<AbstractAction> updateAction(AbstractAction action) {
        AbstractAction abstractAction = findActionById(action.getId()).get();
        abstractAction.setDescription(action.getDescription());
        return Optional.of(abstractAction);
    }
}
