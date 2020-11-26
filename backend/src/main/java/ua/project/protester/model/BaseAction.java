package ua.project.protester.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@Getter
@Setter
@ToString
public class BaseAction {

    protected Integer id;
    protected Integer declarationId;
    protected String name;
    protected ActionType type;
    protected String description;
    protected String[] parameterNames;
    protected Map<String, String> preparedParams;

    public void invoke(Map<String, String> params, WebDriver driver) {
    }

    public void invoke(WebDriver driver) {
        invoke(preparedParams, driver);
    }

    public void invoke(Map<String, String> params) {
    }
}
