package ua.project.protester.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;

import java.util.Map;

@Action(
        name = "Set ${key} = label of element with ${xpath} in context",
        type = ExecutableComponentType.TECHNICAL,
        description = "Save element label to context",
        parameterNames = {"key", "xpath"}
)
public class SaveLabelByXPathAction extends AbstractAction {
    @Override
    protected ActionResultTechnicalDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, JdbcTemplate jdbcTemplate, Environment environment, RestTemplate restTemplate) {
        try {
            String text = driver.findElement(By.xpath(params.get("xpath"))).getText();
            context.put(params.get("key"), text);
            return new ActionResultTechnicalDto(Map.of("label", text));
        } catch (Exception e) {
            return new ActionResultTechnicalDto(new ActionExecutionException(e.getMessage()));
        }
    }
}
