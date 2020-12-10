package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.ActionResult;

import java.util.Map;

@Action(
        type = ExecutableComponentType.TECHNICAL,
        description = "C description",
        parameterNames = {"c"}
)
public class ActionC extends AbstractAction {
    @Override
    protected void logic(Map<String, String> params, WebDriver driver, ActionResult result) throws ActionExecutionException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new ActionExecutionException();
        }
        System.out.println("C with param: " + params.get("c"));
    }

    @Override
    public void execute(Map<String, String> params, WebDriver driver) {

    }
}
