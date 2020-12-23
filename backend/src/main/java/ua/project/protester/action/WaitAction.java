package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import org.springframework.web.client.RestTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;

import java.util.Map;

@Action(
        name = "Wait ${n} seconds",
        type = ExecutableComponentType.TECHNICAL,
        description = "Wait the specified amount of time",
        parameterNames = {"n"}
)
public class WaitAction extends AbstractAction {
    @Override
    protected ActionResultTechnicalDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, Environment environment, RestTemplate restTemplate) {
        try {
            Thread.sleep(Integer.parseInt(params.get("n")) * 1000);
            return new ActionResultTechnicalDto();
        } catch (Exception e) {
            return new ActionResultTechnicalDto(new ActionExecutionException(e.getMessage()));
        }
    }
}
