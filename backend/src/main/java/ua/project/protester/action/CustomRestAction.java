package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.ActionResultDto;

import java.util.Map;

@Action(
        name = "Send ${method} request to url ${url} with body ${body}",
        type = ExecutableComponentType.REST,
        description = "Send request with the specified method to the specified url with the specified body",
        parameterNames = {"method", "url", "body"}
)
public class CustomRestAction extends AbstractAction {

    @Override
    protected ActionResultDto logic(Map<String, String> params, Map<String, String> context, Environment environment, WebDriver driver) {
        return null;
    }

}
