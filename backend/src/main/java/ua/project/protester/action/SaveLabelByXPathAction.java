package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.ActionResultDto;

import java.util.Map;

@Action(
        name = "Set ${key} = label of element with ${xpath}",
        type = ExecutableComponentType.TECHNICAL
)
public class SaveLabelByXPathAction extends AbstractAction {
    @Override
    protected ActionResultDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, JdbcTemplate jdbcTemplate, Environment environment, RestTemplate restTemplate) {
        return null;
    }
}
