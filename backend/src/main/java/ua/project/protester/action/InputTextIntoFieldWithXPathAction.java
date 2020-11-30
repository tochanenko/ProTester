package ua.project.protester.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.AbstractAction;

import java.util.Map;

@Action(
        type = ExecutableComponentType.TECHNICAL,
        description = "Input specified text into field with specified xpath",
        parameterNames = {"text", "xpath"}
)
public class InputTextIntoFieldWithXPathAction extends AbstractAction {
    @Override
    public void execute(Map<String, String> params, WebDriver driver) {
        driver.findElement(By.xpath(params.get("xpath"))).sendKeys(params.get("text"));
    }
}
