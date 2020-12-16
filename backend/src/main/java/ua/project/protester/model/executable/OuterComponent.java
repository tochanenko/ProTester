package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.exception.executable.action.IllegalActionLogicImplementation;
import ua.project.protester.model.executable.result.ActionResultDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Setter
@Getter
@ToString
public class OuterComponent extends ExecutableComponent {

    protected List<Step> steps;

    protected static boolean isParameterPlaceholder(String parameter) {
        return parameter.startsWith("${") && parameter.endsWith("}");
    }

    protected static String extractParameterValue(String parameter) {
        return isParameterPlaceholder(parameter)
                ?
                parameter.substring(2, parameter.length() - 1)
                :
                parameter;
    }

    protected Map<String, String> fitInputParameters(Map<String, String> inputParameters, Map<String, String> mapping) {
        Map<String, String> result = new HashMap<>();
        mapping.forEach((key, value) -> {
            if (isParameterPlaceholder(value)) {
                result.put(key, inputParameters.get(extractParameterValue(value)));
            } else {
                result.put(key, value);
            }
        });
        return result;
    }

    public String[] getParameterNames() {
        if (parameterNames != null) {
            return parameterNames;
        }

        parameterNames = steps
                .stream()
                .map(Step::getParameters)
                .flatMap(map -> map.values().stream())
                .filter(OuterComponent::isParameterPlaceholder)
                .distinct()
                .map(OuterComponent::extractParameterValue)
                .toArray(String[]::new);
        return parameterNames;
    }

    @Override
    public void execute(Map<String, String> params, Map<String, String> context, WebDriver driver, Consumer<ActionResultDto> callback) throws ActionExecutionException, IllegalActionLogicImplementation {
        for (Step step : steps) {
            step.getComponent().execute(
                    fitInputParameters(params, step.getParameters()),
                    context,
                    driver,
                    callback);
        }
    }

    public void execute(Map<String, String> params, WebDriver driver, Consumer<ActionResultDto> callback) throws ActionExecutionException, IllegalActionLogicImplementation {
        for (Step step : steps) {
            step.getComponent().execute(
                    fitInputParameters(params, step.getParameters()),
                    new HashMap<>(),
                    driver,
                    callback);
        }
    }
}
