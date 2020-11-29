package ua.project.protester.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.project.protester.exception.ActionMapperException;
import ua.project.protester.model.BaseAction;
import ua.project.protester.repository.ActionRepository;
import ua.project.protester.request.ActionRequestModel;

@Component
public class ActionMapper {

    private ActionRepository actionRepository;

    @Autowired
    public ActionMapper(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public BaseAction toBaseActionFromActionRequest(ActionRequestModel action) {
        if (action != null) {
            BaseAction baseAction = actionRepository.findActionByDeclarationId(action.getDeclarationId())
                    .orElseThrow(() -> new ActionMapperException("Action" + action.getName() + "was'nt found"));
            baseAction.prepare(action.getPreparedParams());
            baseAction.setDescription(action.getDescription());
            return baseAction;
        }
        return null;
    }
}
