package ua.project.protester.service;

import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ua.project.protester.exception.ActionNotFoundException;
import ua.project.protester.model.BaseAction;
import ua.project.protester.repository.ActionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActionService {

    private  ActionRepository actionRepository;

    private WebDriver driver;

    public ActionService(ActionRepository actionRepository, @Lazy WebDriver driver) {
        this.actionRepository = actionRepository;
        this.driver = driver;
    }

    public void invoke(BaseAction action) {
        try {

            BaseAction baseAction = actionRepository.findActionById(action.getId());
            Map<String, String> params = new HashMap<>();

            for(String str : action.getParameterNames()){

                String[] parts = str.split(":");

                params.put( parts[0].trim(), parts[1].trim() );
            }

            baseAction.invoke(params,driver);

        } catch (ActionNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<BaseAction> findAll() {
        return actionRepository.findAllActions();
    }

    public BaseAction findActionByName(String name) {
        try {
            return actionRepository.findActionByName(name);
        } catch (ActionNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseAction findActionByType(String type) {
        try {
            return actionRepository.findActionByType(type);
        } catch (ActionNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BaseAction> findActionsPagination(int pageSize, int pageNumber) {
        return actionRepository.findActionsPagination(pageSize,pageNumber);
    }
}
