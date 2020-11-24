package ua.project.protester.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.ActionType;
import ua.project.protester.model.BaseAction;

import java.util.Map;

@Action(
        name = "ClickOnElementWithXPath",
        type = ActionType.TECHNICAL,
        description = "Click on element with specified xpath",
        parameterNames = {"xpath"}
)
public class ClickOnElementWithXPathAction extends BaseAction {
    @Override
    public void invoke(Map<String, String> params, WebDriver driver) {
        driver.findElement(By.xpath(params.get("xpath"))).click();
    }
}
