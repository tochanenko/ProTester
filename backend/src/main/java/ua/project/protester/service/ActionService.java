package ua.project.protester.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.ActionMapperException;
import ua.project.protester.exception.executable.action.ActionNotFoundException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.repository.ActionRepository;
import ua.project.protester.request.ActionFilter;
import ua.project.protester.request.ActionRequestModel;
import ua.project.protester.utils.ActionMapper;
import ua.project.protester.utils.Page;

import java.util.List;


@Service
public class ActionService {

    private  ActionRepository actionRepository;
    private  ActionMapper actionMapper;
    private  WebDriver driver;

    @Autowired
    public ActionService(ActionRepository actionRepository, ActionMapper actionMapper, @Lazy WebDriver driver) {
        this.actionRepository = actionRepository;
        this.actionMapper = actionMapper;
        this.driver = driver;
    }

    @Transactional
    public void invoke(List<ActionRequestModel> actions) {
        actions.stream()
                .map(action -> actionMapper.toAbstractActionFromActionRequest(action)
                .orElseThrow(() -> new ActionMapperException("Action" + action.getName() + "was`nt mapped")))
                .forEach(baseAction -> baseAction.execute(driver));
    }

    @Transactional
    public AbstractAction findActionByActionId(Integer id) {
        return actionRepository.findActionById(id)
                .orElseThrow(ActionNotFoundException::new);
    }

    @Transactional
    public AbstractAction findActionByClassName(String className) {
        return actionRepository.findActionByClassName(className)
                .orElseThrow(ActionNotFoundException::new);
    }

    @Transactional
    public Page<AbstractAction> findAllProjects(int pageSize, int pageNumber, String  actionName, ExecutableComponentType type) {
        return actionRepository.findAllActions(new ActionFilter(pageSize, pageNumber, actionName, type));
    }

    @Transactional
    public AbstractAction updateDescription(Integer id, String newDescription) {
        return actionRepository.updateActionDescriptionById(id, newDescription)
                .orElse(null);
    }

}
