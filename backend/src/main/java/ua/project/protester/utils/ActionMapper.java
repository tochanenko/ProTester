package ua.project.protester.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.project.protester.exception.ActionMapperException;
import ua.project.protester.exception.executable.action.ActionNotFoundException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.repository.ActionRepository;
import ua.project.protester.request.ActionRequestModel;

import java.util.Optional;

@Component
public class ActionMapper {

    private ActionRepository actionRepository;

    @Autowired
    public ActionMapper(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public Optional<AbstractAction> toAbstractActionFromActionRequest(ActionRequestModel action) {
        if (action != null) {
            try {
                return Optional.of(actionRepository.findActionById(action.getId()));
            } catch (ActionNotFoundException e) {
                e.printStackTrace();
                throw new ActionMapperException("Action" + action.getName() + "wasn't found");
            }
        }
        return Optional.empty();
    }
}
