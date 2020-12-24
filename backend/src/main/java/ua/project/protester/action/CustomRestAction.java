package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import org.springframework.http.HttpEntity;
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
        name = "Send ${method} request to url ${url} with body ${body}",
        type = ExecutableComponentType.REST,
        description = "Send request with the specified method to the specified url with the specified body",
        parameterNames = {"method", "url", "body"}
)
public class CustomRestAction extends AbstractAction {

    @Override
    protected ActionResultRestDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, Environment environment, RestTemplate restTemplate) {
        String requestBody = params.get("body");
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    params.get("url"),
                    HttpMethod.valueOf(params.get("method")),
                    new HttpEntity<>(requestBody),
                    String.class);

            return new ActionResultRestDto(
                    requestBody,
                    response.getBody() != null ? response.getBody() : "",
                    response.getStatusCodeValue());

        } catch (Exception e) {
            return new ActionResultRestDto(
                    new ActionExecutionException(e.getMessage()),
                    requestBody,
                    "",
                    0);
        }
    }
}
