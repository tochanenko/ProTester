package ua.project.protester.action;

import okhttp3.OkHttpClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;

import java.util.Map;

@Action(
        name = "Get action by url ${url}",
        type = ExecutableComponentType.TECHNICAL,
        description = "Performs get method on specified url",
        parameterNames = {"url"}
)
public class GoToUrlAction extends AbstractAction {
    @Override
    protected ActionResultTechnicalDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, OkHttpClient httpClient) {

        try {
            System.out.println("RESULT  " + params.get("url"));
            driver.navigate().to(params.get("url"));
            return new ActionResultTechnicalDto();
        } catch (WebDriverException e) {
            return new ActionResultTechnicalDto(new ActionExecutionException(e.getMessage()));
        }
    }
}
