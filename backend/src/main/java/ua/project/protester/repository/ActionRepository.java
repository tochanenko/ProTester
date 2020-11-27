package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.ActionDeclarationNotFoundException;
import ua.project.protester.exception.ActionImplementationNotFoundException;
import ua.project.protester.exception.ActionNotFoundException;
import ua.project.protester.exception.PreparedActionWithoutParametersException;
import ua.project.protester.model.ActionDeclaration;
import ua.project.protester.model.BaseAction;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@PropertySource("classpath:queries/action.properties")
@RequiredArgsConstructor
public class ActionRepository {

    private static final String PROPERTY_NOT_FOUND_TEMPLATE = "Could not find property '%s' in queries/action.properties";
    private final Logger logger = LoggerFactory.getLogger(ActionRepository.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final ActionDeclarationRepository actionDeclarationRepository;
    private final ActionParameterRepository actionParameterRepository;

    @Transactional
    public Integer save(BaseAction action) throws PreparedActionWithoutParametersException {
        String propertyName = "saveAction";
        try {
            if (!action.isPrepared()) {
                throw new PreparedActionWithoutParametersException();
            }

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new BeanPropertySqlParameterSource(action),
                    keyHolder,
                    new String[]{"action_id"});
            actionParameterRepository.saveParameters((Integer) keyHolder.getKey(), action.getPreparedParams());
            return (Integer) keyHolder.getKey();
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
            return 0;
        }
    }

    public List<BaseAction> findAllPreparedActions() throws ActionImplementationNotFoundException {
        String propertyName = "findAllPreparedActionsId";
        try {
            return namedParameterJdbcTemplate.queryForList(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new MapSqlParameterSource(),
                    Integer.class)
                    .stream()
                    .map(this::findPreparedActionById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
            return Collections.emptyList();
        }
    }

    public Optional<BaseAction> findPreparedActionById(Integer id) {
        try {
            ActionDeclaration actionDeclaration = actionDeclarationRepository
                    .findActionDeclarationByActionId(id)
                    .orElseThrow(ActionDeclarationNotFoundException::new);
            BaseAction action = getPreparedActionImplementationByDeclarationAndId(actionDeclaration, id);
            return Optional.of(action);
        } catch (ActionDeclarationNotFoundException e) {
            logger.warn("Failed to find action declaration");
            return Optional.empty();
        }
    }

    public Optional<BaseAction> findActionByDeclarationId(Integer declarationId) {
        try {
            ActionDeclaration actionDeclaration = actionDeclarationRepository
                    .findActionDeclarationById(declarationId)
                    .orElseThrow(ActionDeclarationNotFoundException::new);
            BaseAction action = getActionImplementationByDeclaration(actionDeclaration);
            return Optional.of(action);
        } catch (ActionDeclarationNotFoundException e) {
            logger.warn("Failed to find action declaration");
            return Optional.empty();
        }
    }

    private BaseAction getActionImplementationByDeclaration(ActionDeclaration actionDeclaration) {
        try {
            Class<?> actionClass = Class.forName(actionDeclaration.getClassName());
            BaseAction action = (BaseAction) actionClass.getConstructor().newInstance();
            Action actionMetadata = actionClass.getAnnotation(Action.class);
            action.init(
                    actionDeclaration,
                    actionMetadata.parameterNames());
            return action;
        } catch (Exception e) {
            throw new ActionImplementationNotFoundException(
                    "Failed to load action implementation for class " + actionDeclaration.getClassName());
        }
    }

    private BaseAction getPreparedActionImplementationByDeclarationAndId(ActionDeclaration actionDeclaration, Integer id) {
        String propertyName = "findActionDescriptionById";
        try {
            Class<?> actionClass = Class.forName(actionDeclaration.getClassName());
            BaseAction action = (BaseAction) actionClass.getConstructor().newInstance();
            Action actionMetadata = actionClass.getAnnotation(Action.class);
            String description = Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new MapSqlParameterSource()
                            .addValue("id", id),
                    String.class))
                    .orElseThrow(ActionNotFoundException::new);
            Map<String, String> parameters = actionParameterRepository.findAllParametersByActionId(id);
            action.init(
                    id,
                    actionDeclaration,
                    description,
                    actionMetadata.parameterNames(),
                    parameters);
            return action;
        } catch (Exception e) {
            throw new ActionImplementationNotFoundException(
                    "Failed to load action implementation for class " + actionDeclaration.getClassName());
        }
    }
}
