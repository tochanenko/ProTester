package ua.project.protester.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.ActionNotFoundException;
import ua.project.protester.exception.PreparedActionWithoutParametersException;
import ua.project.protester.model.BaseAction;
import ua.project.protester.repository.ActionRepository;
import ua.project.protester.request.ActionRequestModel;
import ua.project.protester.utils.ActionMapper;

import java.util.List;
import java.util.stream.Collectors;

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
    public void invokeAllPreparedStatements() {
        actionRepository.findAllPreparedActions().forEach(baseAction -> baseAction.invoke(driver));
    }

    @Transactional
    public void prepareAndSaveActions(List<ActionRequestModel> action) {

        List<BaseAction> actions = action
                .stream()
                .map(actionMapper::toBaseActionFromActionRequest)
                .collect(Collectors.toList());
        actions.forEach(preparedAction -> {
            try {
                actionRepository.save(preparedAction);
            } catch (PreparedActionWithoutParametersException e) {
                e.printStackTrace();
            }
        });
    }

    @Transactional
    public void invokePreparedActionByActionId(Integer id) {
        actionRepository.findPreparedActionById(id)
                .orElseThrow(() -> new ActionNotFoundException("Action was not find"))
                .invoke(driver);
    }

    @Transactional
    public BaseAction findPreparedActionByActionId(Integer id) {
        return actionRepository.findPreparedActionById(id).orElseThrow(ActionNotFoundException::new);
    }

    @Transactional
    public List<BaseAction> findAllEmptyActions() {
        return actionRepository.findEmptyActions();
    }

    @Transactional
    public List<BaseAction> findAllPreparedActions() {
        return actionRepository.findAllPreparedActions();
    }

    @Transactional
    public void deletePreparedActionByActionId(Integer id) {
        actionRepository.deletePreparedActionByActionId(id);
    }
}
