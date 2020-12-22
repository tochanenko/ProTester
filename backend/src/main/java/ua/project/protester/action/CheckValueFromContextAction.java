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
        name = "Get value from the context by key ${key} and check if it equals ${value}",
        type = ExecutableComponentType.TECHNICAL,
        description = "Check if context variables value equals provided value",
        parameterNames = {"key", "value"}
)
public class CheckValueFromContextAction extends AbstractAction {

    @Override
    protected ActionResultTechnicalDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, OkHttpClient okHttpClient) {
        try {
            String contextValue = context.get(params.get("key"));
            String providedValue = params.get("value");

            if (providedValue.equals(contextValue)) {
                return new ActionResultTechnicalDto();
            } else {
                return new ActionResultTechnicalDto(
                        new ActionExecutionException(String.format(
                                "Check failed. Provided: '%s'. Actual: '%s'",
                                providedValue,
                                contextValue)),
                        Map.of(
                                "provided", providedValue,
                                "actual", contextValue));
            }
        } catch (Exception e) {
            return new ActionResultTechnicalDto(new ActionExecutionException(e.getMessage()));
        }
    }
}
