package ua.project.protester.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.project.protester.exception.ActionMapperException;
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
            AbstractAction baseAction = actionRepository.findActionById(action.getId())
                    .orElseThrow(() -> new ActionMapperException("Action" + action.getName() + "was'nt found"));
//            baseAction.prepare(action.getPreparedParams());
            return Optional.of(baseAction);
        }
        return Optional.empty();
    }
}
