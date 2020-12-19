package ua.project.protester.action;

import okhttp3.OkHttpClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;

import java.util.Map;

@Action(
        type = ExecutableComponentType.TECHNICAL,
        description = "Input ${text} into field with specified ${xpath}",
        parameterNames = {"text", "xpath"}
)
public class InputTextIntoFieldWithXPathAction extends AbstractAction {
    @Override
    protected ActionResultTechnicalDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, OkHttpClient okHttpClient) {
        try {
            driver.findElement(By.xpath(params.get("xpath"))).sendKeys(params.get("text"));
            return new ActionResultTechnicalDto();
        } catch (WebDriverException ex) {
            System.out.println(ex.getClass().getName());
            return new ActionResultTechnicalDto(new ActionExecutionException(ex.getMessage()));
        }
    }
}
