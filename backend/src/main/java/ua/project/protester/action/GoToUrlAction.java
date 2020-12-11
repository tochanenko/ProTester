package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.result.ActionResult;

import java.util.Map;

@Action(
        type = ExecutableComponentType.REST,
        description = "Performs get method on specified url",
        parameterNames = {"url"}
)
public class GoToUrlAction extends AbstractAction {
    @Override
    protected void logic(Map<String, String> params, Map<String, String> context, WebDriver driver, ActionResult result) {

        try {
            System.out.println("RESULT  " + params.get("url"));
            driver.navigate().to(params.get("url"));
        } catch (WebDriverException ex) {
            System.out.println(ex.getClass().getName());
        }
    }
}
