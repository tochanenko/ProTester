package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.actions.ClickAction;
import ua.project.protester.actions.WaitAction;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.ActionImplementationNotFoundException;
import ua.project.protester.exception.ActionNotFoundException;
import ua.project.protester.exception.NotUniqueActionNameException;
import ua.project.protester.model.BaseAction;

import java.util.*;

@Repository
@PropertySource("classpath:queries/action.properties")
@RequiredArgsConstructor
public class ActionRepository {

    private static final String PROPERTY_NOT_FOUND_TEMPLATE = "Could not find property '%s' in queries/action.properties";
    private final Logger logger = LoggerFactory.getLogger(ActionRepository.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private List<BaseAction> actions;

    private static void connectAction(BaseAction dbAction, List<BaseAction> actionsInCode) {
        BaseAction action = actionsInCode
                .stream()
                .filter(codeAction -> codeAction.hasSameSignature(dbAction))
                .findFirst()
                .orElseThrow(() -> new ActionImplementationNotFoundException(
                        "Failed to found " + dbAction.getName() + " action implementation in code"));

        action.setId(dbAction.getId());
        action.setDescription(dbAction.getDescription());
    }

    public void syncWithDb() {
        List<BaseAction> actionsInCode = scanForActions();
        List<BaseAction> actionsInDb = loadActionsFromDb();
        actions = combine(actionsInCode, actionsInDb);
    }

    private List<BaseAction> scanForActions() {
        List<BaseAction> actionsInCode = new ArrayList<>();
        Set<String> uniqueNames = new HashSet<>();
        Reflections reflections = new Reflections("ua.project.protester.action");
        Set<Class<?>> actionCandidates = reflections.getTypesAnnotatedWith(Action.class);
        for (Class<?> actionCandidate : actionCandidates) {
            try {
                Action actionMetadata = actionCandidate.getAnnotation(Action.class);

                String name;
                if (actionMetadata.name().isEmpty()) {
                    name = actionCandidate.getSimpleName();
                } else {
                    name = actionMetadata.name();
                }

                if (uniqueNames.contains(name)) {
                    throw new NotUniqueActionNameException();
                }
                uniqueNames.add(name);

                BaseAction action = (BaseAction) actionCandidate.getConstructor().newInstance();
                action.init(
                        actionMetadata.type(),
                        name,
                        actionMetadata.description());
                actionsInCode.add(action);
                logger.info("Action loaded: " + action.getName()+action);
            } catch (Exception e) {
                logger.warn("Failed to load action class " + actionCandidate.getSimpleName() + ". Exception : " + e);
            }
        }

        return actionsInCode;
    }

    private List<BaseAction> loadActionsFromDb() {
        String propertyName = "findAllActions";
        try {
            return namedParameterJdbcTemplate.query(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new BeanPropertyRowMapper<>(BaseAction.class));
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
            return Collections.emptyList();
        }
    }

    private List<BaseAction> combine(List<BaseAction> actionsInCode, List<BaseAction> actionsInDb) {

        actionsInDb
                .forEach(dbAction -> connectAction(dbAction, actionsInCode));

        actionsInCode
                .stream()
                .filter(codeAction -> codeAction.getId() == null)
                .forEach(this::uploadActionToDb);

        return actionsInCode;
    }

    public void uploadActionToDb(BaseAction codeAction) {
        String propertyName = "saveAction";
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(
                    Objects.requireNonNull(env.getProperty("saveAction")),
                    new BeanPropertySqlParameterSource(codeAction),
                    keyHolder,
                    new String[]{"action_id"});
            Number id = keyHolder.getKey();
            codeAction.setId((Integer) id);
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
        }
    }

    public BaseAction findActionById(int id) throws ActionNotFoundException {
        return actions
                .stream()
                .filter(baseAction -> baseAction.getId() == id)
                .findFirst()
                .orElseThrow(ActionNotFoundException::new);
    }

    public BaseAction findActionByName(String name) throws ActionNotFoundException {
        return actions
                .stream()
                .filter(baseAction -> baseAction.getName().equals(name))
                .findFirst()
                .orElseThrow(ActionNotFoundException::new);
    }

    public List<BaseAction> findAllActions() {
        return actions;
    }


}
