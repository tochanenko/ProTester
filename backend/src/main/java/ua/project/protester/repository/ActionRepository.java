package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
            if (action.getPreparedParams() == null || action.getPreparedParams().isEmpty()) {
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
        String propertyName = "findActionById";
        try {
            ActionDeclaration actionDeclaration = actionDeclarationRepository
                    .findActionDeclarationByActionId(id)
                    .orElseThrow(ActionDeclarationNotFoundException::new);
            BaseAction action = Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
                    Objects.requireNonNull(env.getProperty(propertyName)),
                    new MapSqlParameterSource()
                            .addValue("id", id),
                    new BeanPropertyRowMapper<>(BaseAction.class)))
                    .orElseThrow(ActionNotFoundException::new);
            loadActionMetadata(action, actionDeclaration);
            loadActionParameters(action);
            return Optional.of(action);
        } catch (NullPointerException e) {
            logger.warn(String.format(PROPERTY_NOT_FOUND_TEMPLATE, propertyName));
            return Optional.empty();
        } catch (ActionDeclarationNotFoundException e) {
            logger.warn("Failed to find action declaration");
            return Optional.empty();
        } catch (ActionNotFoundException e) {
            logger.warn("Failed to find action");
            return Optional.empty();
        }
    }

    public Optional<BaseAction> findActionByDeclarationId(Integer declarationId) {
        try {
            ActionDeclaration actionDeclaration = actionDeclarationRepository
                    .findActionDeclarationById(declarationId)
                    .orElseThrow(ActionDeclarationNotFoundException::new);
            BaseAction action = getActionImplementationByClassName(actionDeclaration.getClassName());
            loadActionMetadata(action, actionDeclaration);
            return Optional.of(action);
        } catch (ActionDeclarationNotFoundException e) {
            logger.warn("Failed to find action declaration");
            return Optional.empty();
        }
    }

    private void loadActionParameters(BaseAction baseAction) {
        baseAction.setPreparedParams(
                actionParameterRepository
                        .findAllParametersByActionId(baseAction.getId()));
    }

    private void loadActionMetadata(BaseAction action, ActionDeclaration actionDeclaration) {
        try {
            action.setDeclarationId(actionDeclaration.getId());
            action.setName(actionDeclaration.getClassName());
            action.setType(actionDeclaration.getType());
            System.out.println(action.getDescription());
            if (action.getDescription() == null || action.getDescription().isEmpty()) {
                action.setDescription(actionDeclaration.getDefaultDescription());
            }
            Action metadata = Class.forName(actionDeclaration.getClassName()).getAnnotation(Action.class);
            action.setParameterNames(metadata.parameterNames());
        } catch (ClassNotFoundException e) {
            logger.warn("Failed to load metadata for " + actionDeclaration.getClassName());
        }
    }

    private BaseAction getActionImplementationByClassName(String className) {
        try {
            return (BaseAction) Class.forName(className).getConstructor().newInstance();
        } catch (Exception e) {
            throw new ActionImplementationNotFoundException(
                    "Failed to load action implementation for class " + className);
        }
    }
}
