package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.ActionType;
import ua.project.protester.model.BaseAction;

import java.util.Map;

@Action(
        type = ActionType.REST,
        defaultDescription = "Performs get method on specified url",
        parameterNames = {"url"}
)
public class GoToUrlAction extends BaseAction {
    @Override
    public void invoke(Map<String, String> params, WebDriver driver) {
        driver.get(params.get("url"));
    }
}
