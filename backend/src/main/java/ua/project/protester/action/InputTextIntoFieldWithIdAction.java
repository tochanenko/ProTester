package ua.project.protester.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.ActionType;
import ua.project.protester.model.BaseAction;

import java.util.Map;

@Action(
        name = "InputTextIntoFieldWithId",
        type = ActionType.TECHNICAL,
        description = "Input specified text into field with specified id",
        parameterNames = {"text", "id"}
)
public class InputTextIntoFieldWithIdAction extends BaseAction {

    public void invoke(Map<String, String> params, WebDriver driver) {
        //driver.findElement(By.id(params.get("id"))).sendKeys(params.get("text"));

        driver.get("http://demo.guru99.com/");
        WebElement element=driver.findElement(By.xpath("//input[@name='emailid']"));
        element.sendKeys(params.get("id"));

        WebElement button=driver.findElement(By.xpath("//input[@name='btnLogin']"));
        button.click();
    }
}
