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
        name = "Click on element with specified ${xpath}",
        type = ExecutableComponentType.TECHNICAL,
        description = "Click on element with xpath",
        parameterNames = {"xpath"}
)
public class ClickOnElementWithXPathAction extends AbstractAction {
    @Override
    protected ActionResultTechnicalDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, JdbcTemplate jdbcTemplate, Environment environment, RestTemplate restTemplate) {
        try {
            driver.findElement(By.xpath(params.get("xpath"))).click();
            return new ActionResultTechnicalDto();
        } catch (Exception e) {
            return new ActionResultTechnicalDto(new ActionExecutionException(e.getMessage()));
        }
    }
}
