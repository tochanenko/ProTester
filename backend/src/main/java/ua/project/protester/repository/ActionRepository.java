package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.reflections.Reflections;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionImplementationNotFoundException;
import ua.project.protester.exception.executable.action.ActionNotFoundException;
import ua.project.protester.exception.executable.action.IllegalActionLogicImplementation;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ActionRepresentation;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultRestDto;
import ua.project.protester.model.executable.result.subtype.ActionResultSqlDto;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;
import ua.project.protester.model.executable.result.subtype.ActionResultUiDto;
import ua.project.protester.request.ActionFilter;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.PropertyExtractor;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ActionRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final Reflections reflections;

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

    public void initialize() {
        log.info("Starting action repository initialization");

        log.info("Performing action implementations scanning...");
        List<String> actionClassesInCode = scanForActionClassesInCode();
        log.info(String.format(
                "Action scan complete, found %d action implementations",
                actionClassesInCode.size()));

        log.info("Performing action classes loading from database...");
        List<String> actionCLassesInDb = loadAllActionClassesFromDb();
        log.info(String.format(
                "Loaded %d action classes from database. Full list: %s",
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

        log.info("Action repository initialization complete");
    }

    private void validateAction(Class<? extends AbstractAction> actionClass) throws IllegalActionLogicImplementation {
        Action a = getMetadata(actionClass);
        ExecutableComponentType type = getActionType(a);
        checkLogicReturningType(actionClass, type);
        checkNamePlaceholders(a);
    }

    private Action getMetadata(Class<? extends AbstractAction> actionClass) throws IllegalActionLogicImplementation {
        Action actionMetadata = actionClass.getAnnotation(Action.class);
        if (actionMetadata == null) {
            throw new IllegalActionLogicImplementation("Action must be annotated with Action annotation");
        }
        return actionMetadata;
    }

    private ExecutableComponentType getActionType(Action actionMetadata) throws IllegalActionLogicImplementation {
        ExecutableComponentType actionType = actionMetadata.type();
        switch (actionType) {
            case TECHNICAL:
            case UI:
            case SQL:
            case REST:
                return actionType;
            default:
                throw new IllegalActionLogicImplementation("Action has incorrect type. Possible types are TECHNICAL, UI, SQL, REST");
        }
    }

    private void checkLogicReturningType(Class<? extends AbstractAction> actionClass, ExecutableComponentType declaredReturnType) throws IllegalActionLogicImplementation {
        try {
            Class<?> actualReturnTypeClass = actionClass
                    .getDeclaredMethod("logic", Map.class, Map.class, WebDriver.class)
                    .getReturnType();

            switch (declaredReturnType) {
                case TECHNICAL:
                    checkType(actualReturnTypeClass, ActionResultTechnicalDto.class);
                    break;
                case REST:
                    checkType(actualReturnTypeClass, ActionResultRestDto.class);
                    break;
                case SQL:
                    checkType(actualReturnTypeClass, ActionResultSqlDto.class);
                    break;
                case UI:
                    checkType(actualReturnTypeClass, ActionResultUiDto.class);
                    break;
                default:
            }
        } catch (NoSuchMethodException e) {
            log.error("It looks like AbstractAction logic method signature has been updated. Please, update it here too.");
            throw new IllegalActionLogicImplementation("Something bad happened during validation. Check log for details");
        }
    }

    private void checkType(Class<?> actual, Class<?> declared) throws IllegalActionLogicImplementation {
        if (!declared.isAssignableFrom(actual)) {
            throw new IllegalActionLogicImplementation(String.format(
                    "Action logic method must have %s returning type. But %s found",
                    declared.getCanonicalName(),
                    actual.getCanonicalName()));
        }
    }

    private void checkNamePlaceholders(Action actionMetadata) throws IllegalActionLogicImplementation {
        Set<String> declaredParameterNames = Set.of(actionMetadata.parameterNames());

        Matcher matcher = Pattern
                .compile("(?<=\\$\\{)(.+?)(?=})", Pattern.CASE_INSENSITIVE)
                .matcher(actionMetadata.name());

        Set<String> parametersInActionName = matcher
                .results()
                .map(MatchResult::group)
                .collect(Collectors.toSet());

        if (!parametersInActionName.equals(declaredParameterNames)) {
            throw new IllegalActionLogicImplementation(String.format(
                    "Action name must contain all parameter names. Declared parameter names: %s. Parameter names in action name: %s",
                    declaredParameterNames,
                    parametersInActionName));
        }
    }

    private List<String> scanForActionClassesInCode() {
        List<String> actionClassesInCode = new LinkedList<>();
        reflections.getSubTypesOf(AbstractAction.class);
        Set<Class<? extends AbstractAction>> actionCandidates = reflections.getSubTypesOf(AbstractAction.class);
        for (Class<? extends AbstractAction> actionCandidate : actionCandidates) {
            try {
                validateAction(actionCandidate);
                actionClassesInCode.add(actionCandidate.getCanonicalName());
                log.info("Action implementation found: " + actionCandidate.getCanonicalName());
            } catch (IllegalActionLogicImplementation e) {
                log.warn(String.format(
                        "Failed to load action implementation %s. Cause: %s",
                        actionCandidate.getCanonicalName(),
                        e.getMessage()));
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

    private AbstractAction findAction(String propertyName, String parameterKey, Object parameterValue) throws ActionNotFoundException {
        try {
            AbstractAction action = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, propertyName),
                    new MapSqlParameterSource().addValue(parameterKey, parameterValue),
                    (rs, rowNum) -> constructAction(
                            rs.getInt("id"),
                            rs.getString("className"),
                            rs.getString("description")));
            if (action == null) {
                throw new ActionNotFoundException(parameterKey, parameterValue);
            }
            return action;
        } catch (DataAccessException e) {
            log.warn(e.toString());
            throw new ActionNotFoundException(e);
        }
    }

    private AbstractAction constructAction(Integer id, String className, String description) {
        try {
            Class<?> actionClass = Class.forName(className);

            AbstractAction action = (AbstractAction) actionClass.getConstructor().newInstance();
            Action metadata = actionClass.getAnnotation(Action.class);

            action.init(
                    id,
                    metadata.name(),
                    metadata.type(),
                    description == null || description.isEmpty() ? metadata.description() : description,
                    className,
                    metadata.parameterNames());

            return action;
        } catch (Exception e) {
            throw new ActionImplementationNotFoundException(
                    "Failed to load action implementation for class " + className, e);
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

    public AbstractAction findActionById(Integer id) throws ActionNotFoundException {
        return findAction(
                "findActionById",
                "id",
                id);
    }

    public AbstractAction findActionByClassName(String className) throws ActionNotFoundException {
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
        try {
            return Optional.of(findActionById(id));
        } catch (ActionNotFoundException e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }
}
