package ua.project.protester.action;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultUiDto;

import java.io.File;
import java.util.Date;
import java.util.Map;

@Action(
        name = "Take screenshot",
        type = ExecutableComponentType.UI,
        description = "Take screenshot"
)
public class SaveScreenshotAction extends AbstractAction {
    @Override
    protected ActionResultUiDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, JdbcTemplate jdbcTemplate, Environment environment, RestTemplate restTemplate) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String filename = "~\\screenshots\\" + new Date().getTime() + ".png";
            System.out.println(filename);
            File destFile = new File(filename);
            FileUtils.copyFile(screenshot, destFile);
            filename = filename.replace('\\', '/');
            System.out.println(filename);
            return new ActionResultUiDto(filename);
        } catch (Exception e) {
            return new ActionResultUiDto(
                    new ActionExecutionException(e.getMessage()),
                    "");
        }
    }
}
