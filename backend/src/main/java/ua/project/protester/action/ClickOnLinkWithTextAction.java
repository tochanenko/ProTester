package ua.project.protester.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.ActionType;
import ua.project.protester.model.BaseAction;

import java.util.Map;

@Action(
        name = "ClickOnLinkWithText",
        type = ActionType.TECHNICAL,
        description = "Click on link with specified text",
        parameterNames = {"text"}
)
public class ClickOnLinkWithTextAction extends BaseAction {
    @Override
    public void invoke(Map<String, String> params, WebDriver driver) {
        driver.findElement(By.linkText(params.get("text"))).click();
    }
}
