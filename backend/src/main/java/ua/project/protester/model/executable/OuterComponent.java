package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
public class OuterComponent extends ExecutableComponent {

    protected Integer id;
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

    public void print() {
        System.out.println("Compound " + id + " " + name);
        System.out.println("| " + description);
        System.out.println("| " + Arrays.toString(getParameterNames()));
    }

    @Override
    public void execute(Map<String, String> params, WebDriver driver) {
        steps.forEach(step -> step.getComponent().execute(
                fitInputParameters(params, step.getParameters()),
                driver));
    }
}
