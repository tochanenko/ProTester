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
        name = "Click on element with id ${id}",
        type = ExecutableComponentType.TECHNICAL,
        description = "Click on element",
        parameterNames = {"id"}
)
public class ClickOnElementWithIdAction extends AbstractAction {

    @Override
    protected ActionResultTechnicalDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, OkHttpClient httpClient) {
        try {
            driver.findElement(By.id(params.get("id"))).click();
            return new ActionResultTechnicalDto();
        } catch (WebDriverException ex) {
            return new ActionResultTechnicalDto(new ActionExecutionException(ex.getClass().getName()));
        }
    }

}
