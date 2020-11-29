package ua.project.protester.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.ActionType;
import ua.project.protester.model.executable.AbstractAction;

import java.util.Map;

@Action(
        type = ActionType.TECHNICAL,
        description = "Click on element with specified id",
        parameterNames = {"id"}
)
public class ClickOnElementWithIdAction extends AbstractAction {
    @Override
    public void execute(Map<String, String> params, WebDriver driver) {
        driver.findElement(By.id(params.get("id"))).click();
    }
}
