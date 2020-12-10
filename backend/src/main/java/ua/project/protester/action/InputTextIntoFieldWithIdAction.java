package ua.project.protester.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.AbstractAction;

import java.util.Map;

@Action(
        type = ExecutableComponentType.TECHNICAL,
        description = "Input specified text into field with specified id",
        parameterNames = {"text", "id"}
)
public class InputTextIntoFieldWithIdAction extends AbstractAction {
    @Override
    protected void logic(Map<String, String> params, WebDriver driver) {
        try {
            System.out.println("RESULT  id: " + params.get("id") + " text: "+ params.get("text"));
            driver.findElement(By.id(params.get("id"))).sendKeys(params.get("text"));
        } catch (WebDriverException ex) {
            System.out.println(ex.getClass().getName());
        }
    }
}
