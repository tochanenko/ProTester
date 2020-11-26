package ua.project.protester.service;

import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ua.project.protester.exception.PreparedActionWithoutParametersException;
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


        BaseAction baseAction = actionRepository.findActionByDeclarationId(action.getId()).get();
            Map<String, String> params = new HashMap<>();
            for (String str : action.getParameterNames()) {

                String[] parts = str.split(":");

                params.put(parts[0].trim(), parts[1].trim());
            }
            baseAction.setPreparedParams(params);
        try {
            actionRepository.save(baseAction);
        } catch (PreparedActionWithoutParametersException e) {
            e.printStackTrace();
        }
        baseAction.invoke(driver);

    }

    public List<BaseAction> findAll() {
        return actionRepository.findAllPreparedActions();
    }

}
