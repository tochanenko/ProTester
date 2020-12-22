package ua.project.protester.action;

import okhttp3.OkHttpClient;
import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;

import java.util.Map;

@Action(
        name = "Save value ${value} with key ${key} to context",
        type = ExecutableComponentType.TECHNICAL,
        description = "Save the specified pair (key,value) to context",
        parameterNames = {"value", "key"}
)
public class SaveToContextAction extends AbstractAction {
    @Override
    protected ActionResultTechnicalDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, OkHttpClient okHttpClient) {
        try {
            context.put(params.get("key"), params.get("value"));
            return new ActionResultTechnicalDto();
        } catch (Exception e) {
            return new ActionResultTechnicalDto(new ActionExecutionException(e.getMessage()));
        }
    }
}
