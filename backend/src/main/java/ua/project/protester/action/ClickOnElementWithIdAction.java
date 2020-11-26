package ua.project.protester.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.ActionType;
import ua.project.protester.model.BaseAction;

import java.util.Map;

@Action(
        type = ActionType.TECHNICAL,
        defaultDescription = "Click on element with specified id",
        parameterNames = {"id"}
)
public class ClickOnElementWithIdAction extends BaseAction {
    @Override
    public void invoke(Map<String, String> params, WebDriver driver) {
        driver.findElement(By.id(params.get("id"))).click();
    }
}
