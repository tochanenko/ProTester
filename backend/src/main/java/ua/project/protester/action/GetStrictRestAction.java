package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultRestDto;

import java.util.Map;

@Action(
        name = "Send get request to url ${url} and fail if code is 4xx or 5xx",
        type = ExecutableComponentType.REST,
        description = "Send get request to the specified url. Fail if response code is 4xx or 5xx",
        parameterNames = {"url"}
)
public class GetStrictRestAction extends AbstractAction {

    @Override
    protected ActionResultRestDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, Environment environment, RestTemplate restTemplate) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    params.get("url"),
                    HttpMethod.GET,
                    null,
                    String.class);
            if (response.getStatusCodeValue() < 400) {
                return new ActionResultRestDto(
                        "",
                        response.getBody() != null ? response.getBody() : "",
                        response.getStatusCodeValue());
            }
            return new ActionResultRestDto(
                    new ActionExecutionException("Response status code is " + response.getStatusCodeValue()),
                    "",
                    response.getBody() != null ? response.getBody() : "",
                    response.getStatusCodeValue());

        } catch (Exception e) {
            return new ActionResultRestDto(
                    new ActionExecutionException(e.getMessage()),
                    "",
                    "",
                    0);
        }
    }
}
