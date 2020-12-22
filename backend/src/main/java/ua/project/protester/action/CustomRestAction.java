package ua.project.protester.action;

import okhttp3.OkHttpClient;
import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultRestDto;

import java.util.Map;

@Action(
        name = "Send ${method} request to url ${url} with body ${body}",
        type = ExecutableComponentType.REST,
        description = "Send request with the specified method to the specified url with the specified body",
        parameterNames = {"method", "url", "body"}
)
public class CustomRestAction extends AbstractAction {

    @Override
    protected ActionResultRestDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, Environment environment, OkHttpClient httpClient) {
        return new ActionResultRestDto();
    }

}
