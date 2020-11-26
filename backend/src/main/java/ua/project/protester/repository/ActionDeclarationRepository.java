package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.ActionImplementationNotFoundException;
import ua.project.protester.model.ActionDeclaration;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ActionDeclarationRepository {

    private static final String PROPERTY_NOT_FOUND_TEMPLATE = "Could not find property '%s' in queries/action-declaration.properties";
    private final Logger logger = LoggerFactory.getLogger(ActionDeclarationRepository.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;

    private static void connectDbActionDeclaration(ActionDeclaration actionDeclarationInDb, List<ActionDeclaration> actionDeclarationsInCode) {
        ActionDeclaration connectedActionDeclaration = actionDeclarationsInCode
                .stream()
                .filter(actionDeclaration -> actionDeclaration.getClassName().equals(actionDeclarationInDb.getClassName()))
                .findFirst()
                .orElseThrow(() -> new ActionImplementationNotFoundException(
                        "Failed to find " + actionDeclarationInDb.getClassName() + " action implementation."));

        actionDeclarationsInCode.remove(connectedActionDeclaration);
    }

    public void syncWithDb() {
        logger.info("Starting action synchronization");
        logger.info("Performing action implementations scanning...");
        List<ActionDeclaration> actionDeclarationsInCode = scanForActionDeclarationsInCode();
        logger.info(String.format(
                "Action scan complete, found %d action implementations",
                actionDeclarationsInCode.size()));
        logger.info("Performing action declarations loading from DB...");
        List<ActionDeclaration> actionDeclarationsInDb = findAllActionDeclarations();
        logger.info(String.format(
                "Loaded %d action declarations from DB. Full list: %s",
                actionDeclarationsInDb.size(),
                actionDeclarationsInDb));
        logger.info("Performing integrity check...");
        List<ActionDeclaration> newActionDeclarations = checkIntegrity(actionDeclarationsInCode, actionDeclarationsInDb);
        logger.info(String.format(
                "Integrity check complete, found %d action implementations without declarations in DB. Full list: %s",
                newActionDeclarations.size(),
                newActionDeclarations));
        if (newActionDeclarations.size() > 0) {
            logger.info("Performing new declarations uploading to DB...");
            uploadNewActionDeclarationsToDb(newActionDeclarations);
            logger.info("Declarations uploading complete");
        }
        logger.info("Action synchronization complete");
    }

    private List<ActionDeclaration> scanForActionDeclarationsInCode() {
        List<ActionDeclaration> actionsInCode = new ArrayList<>();
        Reflections reflections = new Reflections("ua.project.protester.action");
        Set<Class<?>> actionCandidates = reflections.getTypesAnnotatedWith(Action.class);
        for (Class<?> actionCandidate : actionCandidates) {
            try {
                Action actionMetadata = actionCandidate.getAnnotation(Action.class);
                ActionDeclaration actionDeclaration = new ActionDeclaration(
                        null,
                        actionCandidate.getCanonicalName(),
                        actionMetadata.type(),
                        actionMetadata.defaultDescription());
                actionsInCode.add(actionDeclaration);
                logger.info("Action implementation found: " + actionDeclaration.getClassName());
            } catch (Exception e) {
                logger.warn("Failed to load action class " + actionCandidate.getCanonicalName() + ". Exception : " + e);
            }
        }

        return actionsInCode;
    }

    private List<ActionDeclaration> checkIntegrity(List<ActionDeclaration> actionDeclarationsInCode, List<ActionDeclaration> actionDeclarationsInDb) {

        actionDeclarationsInDb
                .forEach(actionDeclarationInDb -> connectDbActionDeclaration(actionDeclarationInDb, actionDeclarationsInCode));

        return actionDeclarationsInCode;
    }

    private void uploadNewActionDeclarationsToDb(List<ActionDeclaration> newActionDeclarations) {
        newActionDeclarations.forEach(this::uploadNewActionDeclarationToDb);
    }

    private void uploadNewActionDeclarationToDb(ActionDeclaration newActionDeclaration) {
        String propertyName = "saveActionDeclaration";
        try {
            namedParameterJdbcTemplate.update(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new BeanPropertySqlParameterSource(newActionDeclaration));
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
        }
    }

    private void loadActionDeclarationMetadata(ActionDeclaration actionDeclaration) {
        try {
            actionDeclaration.loadMetadata();
        } catch (ClassNotFoundException e) {
            logger.warn("Failed to load metadata for " + actionDeclaration.getClassName());
        }
    }

    public List<ActionDeclaration> findAllActionDeclarations() {
        String propertyName = "findAllActionDeclarations";
        try {
            return namedParameterJdbcTemplate.query(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new BeanPropertyRowMapper<>(ActionDeclaration.class))
                    .stream()
                    .peek(this::loadActionDeclarationMetadata)
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
            return Collections.emptyList();
        }
    }

    public Optional<ActionDeclaration> findActionDeclarationById(Integer id) {
        return findActionDeclaration(
                "findActionDeclarationById",
                "id",
                id);
    }

    public Optional<ActionDeclaration> findActionDeclarationByClassName(String className) {
        return findActionDeclaration(
                "findActionDeclarationByClassName",
                "className",
                className);
    }

    public Optional<ActionDeclaration> findActionDeclarationByActionId(Integer id) {
        return findActionDeclaration(
                "findActionDeclarationByActionId",
                "id",
                id);
    }

    private Optional<ActionDeclaration> findActionDeclaration(String propertyName, String parameterKey, Object parameterValue) {
        try {
            Optional<ActionDeclaration> actionDeclaration = Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(
                            Objects.requireNonNull(env.getProperty(propertyName)),
                            new MapSqlParameterSource().addValue(parameterKey, parameterValue),
                            new BeanPropertyRowMapper<>(ActionDeclaration.class)));
            actionDeclaration.ifPresent(this::loadActionDeclarationMetadata);
            return actionDeclaration;
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
            return Optional.empty();
        } catch (DataAccessException e) {
            logger.warn(e.toString());
            return Optional.empty();
        }
    }
}
