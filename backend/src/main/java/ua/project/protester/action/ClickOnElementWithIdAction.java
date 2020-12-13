package ua.project.protester.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.result.ActionResultDto;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;

import java.util.Map;

@Action(
        type = ExecutableComponentType.TECHNICAL,
        description = "Click on element with specified ${id}",
        parameterNames = {"id"}
)
public class ClickOnElementWithIdAction extends AbstractAction {
    @Override
    protected ActionResultDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver) {
        try {
            driver.findElement(By.id(params.get("id"))).click();
            return new ActionResultTechnicalDto();
        } catch (WebDriverException ex) {
            System.out.println(ex.getClass().getName());
            return new ActionResultTechnicalDto(new ActionExecutionException(ex.getMessage()));
        }
    }
}
