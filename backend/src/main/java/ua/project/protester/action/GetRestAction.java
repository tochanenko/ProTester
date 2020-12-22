package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.ActionResultDto;
import ua.project.protester.model.executable.result.subtype.ActionResultRestDto;

import java.util.Map;

@Action(
        name = "Send get request to url ${url}",
        type = ExecutableComponentType.REST,
        description = "Send get request to the specified url",
        parameterNames = {"url"}
)
public class GetRestAction extends AbstractAction {

    @Override
    protected ActionResultRestDto logic(Map<String, String> params, Map<String, String> context, Environment environment, WebDriver driver) {
        return null;
    }
}
