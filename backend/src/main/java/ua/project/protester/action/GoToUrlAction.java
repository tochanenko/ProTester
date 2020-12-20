package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;

import java.util.Map;

@Action(
        name = "Go to ${url}",
        type = ExecutableComponentType.TECHNICAL,
        description = "Open the specified url",
        parameterNames = {"url"}
)
public class GoToUrlAction extends AbstractAction {
    @Override
    protected ActionResultTechnicalDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver) {

        try {
            driver.navigate().to(params.get("url"));
            return new ActionResultTechnicalDto();
        } catch (WebDriverException ex) {
            System.out.println(ex.getClass().getName());
            return new ActionResultTechnicalDto(new ActionExecutionException(ex.getMessage()));
        }
    }
}
