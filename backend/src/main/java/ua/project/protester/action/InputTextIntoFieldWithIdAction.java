package ua.project.protester.action;

import okhttp3.OkHttpClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;

import java.util.Map;

@Action(
        name = "Input ${text} into field with specified ${id}",
        type = ExecutableComponentType.TECHNICAL,
        description = "Input text into field wtih id action",
        parameterNames = {"text", "id"}
)
public class InputTextIntoFieldWithIdAction extends AbstractAction {
    @Override
    protected ActionResultTechnicalDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, OkHttpClient httpClient) {
        try {
            System.out.println("RESULT  id: " + params.get("id") + " text: " + params.get("text"));
            driver.findElement(By.id(params.get("id"))).sendKeys(params.get("text"));
            return new ActionResultTechnicalDto();
        } catch (WebDriverException ex) {
            System.out.println(ex.getClass().getName());
            return new ActionResultTechnicalDto();
        }
    }
}
