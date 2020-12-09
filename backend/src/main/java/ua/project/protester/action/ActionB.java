package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;

import java.util.Map;

@Action(
        type = ExecutableComponentType.TECHNICAL,
        description = "B description",
        parameterNames = {"b"}
)
public class ActionB extends AbstractAction {

    @Override
    protected void logic(Map<String, String> params, WebDriver driver) throws ActionExecutionException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new ActionExecutionException();
        }
        System.out.println("B with param: " + params.get("b"));
    }
}
