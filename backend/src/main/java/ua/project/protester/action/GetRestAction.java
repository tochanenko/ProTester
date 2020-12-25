package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
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
    protected ActionResultRestDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, JdbcTemplate jdbcTemplate, Environment environment, RestTemplate restTemplate) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    params.get("url"),
                    HttpMethod.GET,
                    null,
                    String.class);
            return new ActionResultRestDto(
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
